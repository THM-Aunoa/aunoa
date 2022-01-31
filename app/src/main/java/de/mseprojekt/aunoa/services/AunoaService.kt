package de.mseprojekt.aunoa.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.telephony.*
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation
import android.text.TextUtils
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.AndroidEntryPoint
import de.mseprojekt.aunoa.R
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRule
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.SpotifyAction
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.cell.CellUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.OperationsUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub.RulesHubUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
import de.mseprojekt.aunoa.feature_app.presentation.MainActivity
import de.mseprojekt.aunoa.other.distanceBetweenTwoPoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.reflect.Method
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

const val INTENT_SCAN_REGION = "Region"
const val INTENT_SCAN_UNTIL = "Until"
const val INTENT_COMMAND = "Command"
const val INTENT_COMMAND_EXIT = "Exit"
const val INTENT_COMMAND_SCAN = "Scan"
const val INTENT_COMMAND_START = "Start"
const val INTENT_COMMAND_UPDATE = "Update"

private const val NOTIFICATION_CHANNEL_GENERAL = "Checking"
private const val CODE_FOREGROUND_SERVICE = 1

private val ACTION_OBJECTS = listOf("VolumeAction", "SpotifyAction")

@ExperimentalMaterialApi
@AndroidEntryPoint
class AunoaService: Service() {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private var requestLocation = false
    private var requestCellId = false
    private var useSpotify = false
    private var run = 0

    private var lastCleanUp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

    private var rules: ArrayList<ArrayList<UnzippedRule>> = ArrayList()

    private val mutex = Mutex()

    private var scanMode = false
    private var scanRegion = -1
    private var scanUntil : LocalDateTime = LocalDateTime.now()

    @Inject
    lateinit var stateUseCases: StateUseCases

    @Inject
    lateinit var cellUseCases: CellUseCases

    @Inject
    lateinit var ruleUseCases: RuleUseCases

    @Inject
    lateinit var rulesHubUseCases: RulesHubUseCases

    @Inject
    lateinit var operationsUseCases: OperationsUseCases

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    private var lastKnownLat: Double? = null
    private var lastKnownLon: Double? = null

    private var currentCellId : Long? = null

    private var cancellationTokenSource = CancellationTokenSource()
    private var isrunning = false
    private var delay : Long = 10000L // 60000L = 1 minute

    private val clientId = "eae6bddb30b249fface969dc4af5efba"
    private val redirectUri = "http://localhost:8080"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onBind(p0: Intent?): IBinder? = null


    @ExperimentalPermissionsApi
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val command = intent.getStringExtra(INTENT_COMMAND)
        if (command == INTENT_COMMAND_EXIT) {
            stopService()
            return START_NOT_STICKY
        }
        if (command == INTENT_COMMAND_UPDATE) {
            CoroutineScope(Dispatchers.Main).launch {
                updateRuleList()
            }
        }
        if (command == INTENT_COMMAND_SCAN) {
            CoroutineScope(Dispatchers.Main).launch {
                val region = intent.getStringExtra(INTENT_SCAN_REGION)
                val regionId = region?.let { cellUseCases.getRegionIdByName(it) }
                val until = intent.getStringExtra(INTENT_SCAN_UNTIL)?.toLong()
                if (region?.isNotEmpty()!! && regionId != null && until != null) {
                    scanMode = true
                    scanUntil = LocalDateTime.ofEpochSecond(
                        until,
                        0,
                        ZoneOffset.UTC
                    )
                    scanRegion = regionId
                } else {
                    scanMode = false
                }
            }
        }
        if (command == INTENT_COMMAND_START && !this.isrunning){
            if (stateUseCases.getCurrentState()) {
                this.isrunning = true
                showNotification()
                runService()
            }
        }

        return START_STICKY
    }

    private fun stopService() {
        this.isrunning = false
        exitSpotify()
        stopForeground(true)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation(): Task<Location> {
        return fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationTokenSource.token
        )
    }

    private fun runService() {
        CoroutineScope(Dispatchers.Main).launch {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            delay(5000)
            updateRuleList()
            Log.d("All rules: ", rules.toString())
            if (requestLocation) {
                requestLocation()
                delay(20000)
            }
            if (requestCellId){
                requestCellId(telephonyManager)
            }
            operationsUseCases.deleteOldOperations()
            mutex.withLock {
                for ((idx, ruleCategories) in rules.withIndex()) {
                    var activeRuleFound = false
                    for (rule in ruleCategories) {
                        val tResult = testTrigger(rule.trigger)
                        if (!tResult) {
                            var aResult = true
                            if (!activeRuleFound) {
                                if (idx==0){
                                    val current = when(audioManager.ringerMode) {
                                        AudioManager.RINGER_MODE_SILENT -> {
                                            0
                                        }
                                        AudioManager.RINGER_MODE_NORMAL -> {
                                            2
                                        }
                                        AudioManager.RINGER_MODE_VIBRATE -> {
                                            1
                                        }
                                        else -> {
                                            -1
                                        }
                                    }
                                    val action = rule.action as VolumeAction
                                    if (current == action.deactivateVolume){
                                        continue
                                    }
                                }
                                Log.d("Action", "Action will be performed (Deactivation)")
                                aResult = performAction(rule.action,audioManager, true)
                                rule.rule.ruleId?.let {
                                    operationsUseCases.insertOperation(
                                        it,
                                        aResult
                                    )
                                }
                                if (aResult){
                                    updateServiceText(rule.action, false)
                                }
                            }
                            if (aResult) {
                                rule.rule.ruleId?.let { ruleUseCases.setActive(false, it) }
                                rule.rule.active = false
                            }
                        } else {
                            var aResult = true
                            if (!activeRuleFound) {
                                Log.d("Action", "Action will be performed (Activation)")
                                aResult = performAction(rule.action, audioManager)
                                rule.rule.ruleId?.let {
                                    operationsUseCases.insertOperation(
                                        it,
                                        aResult
                                    )
                                }
                                if (aResult){
                                    updateServiceText(rule.action, true)
                                }
                            }
                            activeRuleFound = true
                            if (aResult) {
                                rule.rule.ruleId?.let { ruleUseCases.setActive(true, it) }
                                rule.rule.active = true
                            }
                        }
                    }
                }
            }
            delay(delay)
            val regions = cellUseCases.getRegions()
            for (region in regions){
                if(region.scanUntil-LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) > 0){
                    scanMode = true
                    scanUntil = LocalDateTime.ofEpochSecond(region.scanUntil,0, ZoneOffset.UTC)
                    scanRegion = region.regionId!!
                }
            }
            while (isrunning) {
                if (run > 60) {
                    run = 0
                    if (LocalDateTime.now().minusDays(1)
                            .toEpochSecond(ZoneOffset.UTC) > lastCleanUp
                    ) {
                        lastCleanUp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                        operationsUseCases.deleteOldOperations()
                    }
                } else{
                    run++
                }
                if (requestLocation) {
                    requestLocation()
                    delay(20000)
                }
                if (requestCellId){
                    requestCellId(telephonyManager)
                }
                var double = false
                mutex.withLock {
                    for (ruleCategories in rules) {
                        for (rule in ruleCategories) {
                            if (rule.rule.enabled) {
                                var secondCheck = false
                                if(double){
                                    if(rule.trigger is LocationTrigger || rule.trigger is CellTrigger || rule.trigger is WifiTrigger) {
                                        secondCheck = true
                                    }
                                }
                                if (rule.rule.active or secondCheck) {
                                    val tResult = testTrigger(rule.trigger, true)
                                    if (tResult) {
                                        Log.d("Action", "Action will be performed (Deactivation)")
                                        val aResult = performAction(rule.action,audioManager, true)
                                        rule.rule.ruleId?.let {
                                            operationsUseCases.insertOperation(
                                                it,
                                                aResult
                                            )
                                        }
                                        if (aResult) {
                                            rule.rule.ruleId?.let {
                                                ruleUseCases.setActive(
                                                    false,
                                                    it
                                                )
                                            }
                                            rule.rule.active = false
                                            updateServiceText(rule.action, false)
                                            double = true
                                        }
                                    } else {
                                        break
                                    }
                                } else {
                                    val tResult = testTrigger(rule.trigger)
                                    if (tResult) {
                                        Log.d("Action", "Action will be performed (Activation)")
                                        val aResult = performAction(rule.action, audioManager)
                                        rule.rule.ruleId?.let {
                                            operationsUseCases.insertOperation(
                                                it,
                                                aResult
                                            )
                                        }
                                        if (aResult) {
                                            rule.rule.ruleId?.let {
                                                ruleUseCases.setActive(
                                                    true,
                                                    it
                                                )
                                            }
                                            rule.rule.active = true
                                            updateServiceText(rule.action, true)
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                double = false
                delay(delay)
            }
        }
    }

    private fun testTrigger(trigger: TriggerObject, reverse: Boolean = false): Boolean{
        when(trigger){
            is TimeTrigger -> {
                val weekday = if (reverse) trigger.endWeekday else trigger.startWeekday
                if (weekday == LocalDate.now().dayOfWeek){
                    return if (reverse){
                        LocalTime.now().toSecondOfDay() > trigger.endTime
                    } else {
                        var endTime = trigger.endTime
                        if (trigger.endWeekday != trigger.startWeekday){
                            endTime = 86400
                        }
                        LocalTime.now().toSecondOfDay() > trigger.startTime && LocalTime.now().toSecondOfDay() < endTime
                    }
                }
                return false
            }
            is LocationTrigger -> {
                return if (lastKnownLat != null && lastKnownLon !=null) {
                    val distance = distanceBetweenTwoPoints(
                        lastKnownLat!!,
                        lastKnownLon!!,
                        trigger.latitude,
                        trigger.longitude
                    )
                    Log.d("Distance", distance.toString())
                    if (reverse) {
                        distance > trigger.radius
                    } else {
                        trigger.radius >= distance
                    }
                } else {
                    Log.d("Location-Error", "Unable to get current Location")
                    false
                }
            }
            is WifiTrigger ->{
                val cManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (networkInfo != null) {
                    if (networkInfo.isConnected) {
                        val wManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val connectionInfo = wManager.connectionInfo
                        if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.ssid)) {
                            if (trigger.name == connectionInfo.ssid){
                                return !reverse
                            }
                        }
                    }
                    return reverse
                }
                Log.d("Wifi-Error", "No Network Info available")
                return false
            }
            is BluetoothTrigger ->{
                val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val pairedDevices = btManager.adapter?.bondedDevices
                if (pairedDevices != null) {
                    if (pairedDevices.size > 0) {
                        for (device in pairedDevices) {
                            if (trigger.name == device.name){
                                val deviceName = device.name
                                val macAddress = device.address
                                val type = device.type
                                val connected = isConnected(device)
                                Log.i(
                                    " pairedDevices ",
                                    "paired device: $deviceName at $macAddress + $type  + $connected "
                                )
                                return if (reverse){
                                    !connected
                                }else{
                                    connected
                                }
                            }
                        }
                        Log.d("Bluetooth Error", "No Device with given Name found")
                        return false
                    }
                    Log.d("Bluetooth Error", "No Device Found")
                    return false
                }
                Log.d("Bluetooth Error", "No Bluetooth Adapter Found")
                return false
            }
            is CellWithIdsTrigger ->{
                if (currentCellId==null){
                    Log.d("Cell-Error", "Cant read your cell Information")
                    return false
                }else {
                    if (scanMode){
                        if (scanUntil > LocalDateTime.now()) {
                            if (!trigger.cellIds.contains(currentCellId) && scanRegion == trigger.id) {
                                trigger.cellIds.add(currentCellId!!)
                                cellUseCases.insertCell(scanRegion, currentCellId!!)
                                Log.d("Updated Cells", "Add Cell $currentCellId into $scanRegion")
                            }
                        }else{
                            scanMode = false
                        }
                    }
                    if (trigger.cellIds.contains(currentCellId)){
                        return !reverse
                    }
                    return reverse
                }
            }
            is NfcTrigger ->{
                Log.d("Nfc-Error", "Not yet implemented")
                return false
            }
            else -> {
                Log.d("Database-Error", "Unsupported Trigger-Type in Database")
                return false
            }
        }
    }

    private fun performAction(action: ActionObject, audioManager: AudioManager,reverse: Boolean = false): Boolean{
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when(action){
            is VolumeAction -> {
                if (manager.isNotificationPolicyAccessGranted) {
                    when (if (reverse) action.deactivateVolume else action.activateVolume) {
                        0 -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                        1 -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                        2 -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    }
                    return true
                }
                Log.d("Volume-Error", "No rights to change Volume Setting")
                return false
            }
            is SpotifyAction ->{
                if (reverse) {
                    return stop()
                }
                return play(action.playlist)
            }
            else -> {
                Log.d("Database-Error", "Unsupported Action-Type in Database")
                return false
            }
        }
    }

    suspend fun updateRuleList(){
        delay(1000)
        Log.d("Update", "Updating Rule List")
        val gson = Gson()
        try {
            val newRules = ruleUseCases.getRulesWithoutFlow()
            val oldSpotify = useSpotify
            mutex.withLock {
                rules = ArrayList()
                requestLocation = false
                requestCellId = false
                useSpotify = false
                for (i in ACTION_OBJECTS.indices) {
                    rules.add(ArrayList())
                }
                for (newRule in newRules) {
                    if (newRule.content.trig.triggerType == "LocationTrigger") {
                        requestLocation = true
                    }
                    if (newRule.content.trig.triggerType == "CellTrigger") {
                        requestCellId = true
                    }
                    if (newRule.content.act.actionType == "SpotifyAction") {
                        useSpotify = true
                    }
                    var index = -1
                    for (i in ACTION_OBJECTS.indices) {
                        if (ACTION_OBJECTS[i] == newRule.content.act.actionType) {
                            index = i
                        }
                    }
                    if (index == -1) {
                        Log.d("Database-Error", "Unsupported Action Type in Database")
                        return
                    }
                    val act: ActionObject = when (newRule.content.act.actionType) {
                        "VolumeAction" -> {
                            gson.fromJson(
                                newRule.content.act.actionObject,
                                VolumeAction::class.java
                            )
                        }
                        "SpotifyAction" -> {
                            gson.fromJson(
                                newRule.content.act.actionObject,
                                SpotifyAction::class.java
                            )
                        }
                        else -> {
                            Log.d("Database-Error", "Unsupported Action Type in Database")
                            continue
                        }
                    }
                    val trig: TriggerObject = when (newRule.content.trig.triggerType) {
                        "TimeTrigger" -> {
                            gson.fromJson(
                                newRule.content.trig.triggerObject,
                                TimeTrigger::class.java
                            )
                        }
                        "LocationTrigger" -> {
                            gson.fromJson(
                                newRule.content.trig.triggerObject,
                                LocationTrigger::class.java
                            )
                        }
                        "WifiTrigger" -> {
                            gson.fromJson(
                                newRule.content.trig.triggerObject,
                                WifiTrigger::class.java
                            )
                        }
                        "BluetoothTrigger" -> {
                            gson.fromJson(
                                newRule.content.trig.triggerObject,
                                BluetoothTrigger::class.java
                            )
                        }
                        "NfcTrigger" -> {
                            gson.fromJson(
                                newRule.content.trig.triggerObject,
                                NfcTrigger::class.java
                            )
                        }
                        "CellTrigger" -> {
                            val temp = gson.fromJson(
                                newRule.content.trig.triggerObject,
                                CellTrigger::class.java
                            )
                            val id = cellUseCases.getRegionIdByName(temp.name)
                            if (id != null) {
                                val list = mutableListOf<Long>()
                                list.addAll(cellUseCases.getCellIdsByRegion(temp.name))
                                CellWithIdsTrigger(
                                    cellIds = list,
                                    id = id
                                )
                            } else {
                                Log.d("Database-Error", "Unsupported Region Name")
                                continue
                            }
                        }
                        else -> {
                            Log.d("Database-Error", "Unsupported Trigger Type in Database")
                            continue
                        }
                    }

                    val ruleToAppend = UnzippedRule(
                        rule = newRule.rule,
                        action = act,
                        trigger = trig
                    )
                    if (rules[index].size == 0) {
                        rules[index].add(ruleToAppend)
                        continue
                    }
                    if (newRule.rule.priority <= rules[index][rules[index].size - 1].rule.priority) {
                        rules[index].add(rules[index].size, ruleToAppend)
                        continue
                    }
                    for (i in 0 until rules[index].size) {
                        if (newRule.rule.priority > rules[index][i].rule.priority) {
                            rules[index].add(i, ruleToAppend)
                            break
                        }
                    }
                }
            }
            if (oldSpotify && !useSpotify) {
                exitSpotify()
            }
            if (!oldSpotify && useSpotify) {
                startSpotify(this)
                delay(10000)
            }
        } catch (e: Exception){
            Log.d("Update Rule List","Update Error appeared")
        }
    }

    private fun requestLocation(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            requestCurrentLocation().addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    lastKnownLat = task.result.latitude
                    lastKnownLon = task.result.longitude
                    Log.d("Location", "Location update received")
                }
            }
        }
    }

    private fun requestCellId(manager : TelephonyManager){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            currentCellId = getCellId(manager)
        }else {
            Log.d("Cell-Error", "No Permissions to read Current Cell Information")
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCellId(manager: TelephonyManager): Long? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val cellInfoList = manager.allCellInfo
            if (cellInfoList != null && cellInfoList.isNotEmpty()) {
                Log.d("Cell-List-Size: ", cellInfoList.size.toString())
                when (val cellId = cellInfoList[0].cellIdentity) {
                    is CellIdentityCdma -> {
                        return cellId.basestationId.toLong()
                    }
                    is CellIdentityGsm -> {
                        return cellId.cid.toLong()
                    }
                    is CellIdentityLte -> {
                        return cellId.ci.toLong()
                    }
                    is CellIdentityNr -> {
                        return cellId.nci
                    }
                    is CellIdentityTdscdma -> {
                        return cellId.cid.toLong()
                    }
                    is CellIdentityWcdma -> {
                        return cellId.cid.toLong()
                    }
                }
            }
        } else {
            when (val location = manager.cellLocation) {
                is CdmaCellLocation -> {
                    return location.baseStationId.toLong()
                }
                is GsmCellLocation -> {
                    return location.cid.toLong()
                }
            }
        }
        return null
    }

    private fun isConnected(device: BluetoothDevice): Boolean {
        return try {
            val m: Method = device.javaClass.getMethod("isConnected")
            m.invoke(device) as Boolean
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

    @ExperimentalPermissionsApi
    @SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
    private fun showNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val replyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
        try {
            with(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_GENERAL,
                    "Aunoa",
                    IMPORTANCE_LOW
                )
            ) {
                enableLights(false)
                setShowBadge(false)
                enableVibration(false)
                setSound(null, null)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                manager.createNotificationChannel(this)
            }
        } catch (e: Exception) {
            Log.d("Error", "showNotification: ${e.localizedMessage}")
        }

        with(
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_GENERAL)
        ) {
            setTicker(null)
            setContentTitle("Aunoa")
            setContentText("Aunoa Background Task is running")
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.ic_icon)
            priority = IMPORTANCE_LOW
            setContentIntent(replyPendingIntent)
            addAction(
                0, "REPLY", replyPendingIntent
            )
            addAction(
                0, "ACHIEVE", replyPendingIntent
            )
            startForeground(CODE_FOREGROUND_SERVICE, build())
            curNotificationBuilder = this
        }
    }

    private fun updateServiceText(action: ActionObject, activation: Boolean = true){
        val localDate = LocalDateTime.now() //For reference
        val formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm")
        val formattedString = localDate.format(formatter)
        val text = when(action){
            is VolumeAction ->{
                if (activation) {
                    "Volume-Action was performed at: $formattedString"
                } else {
                    "Volume-Deactivation was performed at: $formattedString"
                }
            }

            is SpotifyAction ->{
                if (activation) {
                    "Spotify-Action was performed at: $formattedString"
                } else {
                    "Spotify-Deactivation was performed at: $formattedString"
                }
            }
            else ->{
                if (activation) {
                    "Action was performed at: $formattedString"
                } else {
                    "Deactivation was performed at: $formattedString"
                }
            }
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val notification = curNotificationBuilder.setContentText(text)
        notificationManager.notify(CODE_FOREGROUND_SERVICE, notification.build())
    }

    private fun play(song: String): Boolean {
        spotifyAppRemote?.let {
            val playlistURI = "spotify:playlist:$song"
            it.playerApi.play(playlistURI)
            return true
        }
        return false
    }
    private fun stop(): Boolean {
        spotifyAppRemote?.let {
            it.playerApi.pause()
            return true
        }
        return false
    }


    private fun startSpotify(service: AunoaService){
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(service, connectionParams, object : ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("Spotify", "Connected!")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("Spotify-Error", throwable.message, throwable)
            }
        })
    }

    private fun exitSpotify() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }
}