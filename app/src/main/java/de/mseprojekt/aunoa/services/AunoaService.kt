package de.mseprojekt.aunoa.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import de.mseprojekt.aunoa.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.media.AudioManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.LocationTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TimeTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.other.distanceBetweenTwoPoints
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRule
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.OperationsUseCases
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


const val INTENT_COMMAND = "Command"
const val INTENT_COMMAND_EXIT = "Exit"
const val INTENT_COMMAND_REPLY = "Reply"
const val INTENT_COMMAND_ACHIEVE = "Achieve"
const val INTENT_COMMAND_START = "Start"

private const val NOTIFICATION_CHANNEL_GENERAL = "Checking"
private const val CODE_FOREGROUND_SERVICE = 1
private const val CODE_REPLY_INTENT = 2
private const val CODE_ACHIEVE_INTENT = 3

private val ACTION_OBJECTS = listOf("VolumeAction")



@AndroidEntryPoint
class AunoaService: Service() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private var requestLocation = false
    private var rules: ArrayList<ArrayList<UnzippedRule>> = ArrayList()

    private val mutex = Mutex()

    @Inject
    lateinit var ruleUseCases: RuleUseCases

    @Inject
    lateinit var operationsUseCases: OperationsUseCases

    private var lastKnownLat: Double? = null
    private var lastKnownLon: Double? = null

    private var cancellationTokenSource = CancellationTokenSource()
    private var isrunning = false
    private var delay : Long = 10000L // 60000L = 1 minute

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val command = intent.getStringExtra(INTENT_COMMAND)
        if (command == INTENT_COMMAND_EXIT) {
            stopService()
            return START_NOT_STICKY
        }
        if (command == INTENT_COMMAND_START && !this.isrunning){
            this.isrunning = true
            showNotification()
            runService()
        }
        if (command == INTENT_COMMAND_REPLY) {
            // Todo open app
            Toast.makeText(this, "Clicked in Notification", Toast.LENGTH_SHORT).show()
        }

        return START_STICKY
    }

    private fun stopService() {
        this.isrunning = false
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

    @SuppressLint("MissingPermission")
    private fun requestLastKnownLocation(): Task<Location> {
        return fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_NO_POWER,
            cancellationTokenSource.token
        )
    }

    private fun runService() {
        CoroutineScope(Dispatchers.Main).launch {
            val gson = Gson()

            var xx: TriggerObject = TimeTrigger(
                startTime = LocalTime.now().toSecondOfDay()-120,
                endTime = LocalTime.now().toSecondOfDay()+120,
                startWeekday = LocalDate.now().dayOfWeek,
                endWeekday = LocalDate.now().dayOfWeek,
            )
            var yy: ActionObject = VolumeAction(
                activateVolume = 0,
                deactivateVolume = 2
            )
            ruleUseCases.insertRule(
                trigger = xx,
                action = yy,
                triggerObjectName = "TimeTrigger",
                actionObjectName = "VolumeAction",
                title = "Test123",
                description = "Test123",
                priority = 10,
            )

            /*

            var xx: TriggerObject = LocationTrigger(
                latitude = 50.54948477682841,
                longitude = 8.613110570286805,
                radius = 400.0
            )
            var yy: ActionObject = VolumeAction(
                activateVolume = 2,
                deactivateVolume = 0
            )
            ruleUseCases.insertRule(
                trigger = xx,
                action = yy,
                triggerObjectName = "LocationTrigger",
                actionObjectName = "VolumeAction",
                title = "Test123",
                description = "Test123",
                priority = 10,
            )
             */


            updateRuleList(gson)
            Log.d("a", rules.toString())
            if (requestLocation) {
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
                    delay(20000)
                }
            }
            mutex.withLock {
                for (ruleCategories in rules) {
                    var activeRuleFound = false
                    for (rule in ruleCategories) {
                        val tResult = testTrigger(rule.trigger)
                        if (!tResult) {
                            var aResult = true
                            if (!activeRuleFound) {
                                Log.d("Action", "Action will be performed (Deactivation)")
                                aResult = performAction(rule.action, true)
                                rule.rule.ruleId?.let {
                                    operationsUseCases.insertOperation(
                                        it,
                                        aResult
                                    )
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
                                aResult = performAction(rule.action)
                                rule.rule.ruleId?.let {
                                    operationsUseCases.insertOperation(
                                        it,
                                        aResult
                                    )
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
            while (isrunning) {
                if (requestLocation) {
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
                        delay(20000)
                    }
                }
                mutex.withLock {
                    for (ruleCategories in rules) {
                        for (rule in ruleCategories) {
                            if (rule.rule.enabled) {
                                if (rule.rule.active) {
                                    val tResult = testTrigger(rule.trigger, true)
                                    if (tResult) {
                                        Log.d("Action", "Action will be performed (Deactivation)")
                                        val aResult = performAction(rule.action, true)
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
                                        }
                                    } else {
                                        break
                                    }
                                } else {
                                    val tResult = testTrigger(rule.trigger)
                                    if (tResult) {
                                        Log.d("Action", "Action will be performed (Activation)")
                                        val aResult = performAction(rule.action)
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
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
            else -> {
                Log.d("Database-Error", "Unsupported Trigger-Type in Database")
                return false
            }
        }
    }


    private fun performAction(action: ActionObject, reverse: Boolean = false): Boolean{
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when(action){
            is VolumeAction -> {
                if (manager.isNotificationPolicyAccessGranted) {
                    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
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
            else -> {
                Log.d("Database-Error", "Unsupported Action-Type in Database")
                return false
            }
        }
    }

    suspend fun updateRuleList(gson: Gson){
        val newRules = ruleUseCases.getRulesWithoutFlow()
        mutex.withLock {
            rules = ArrayList()
            requestLocation = false
            for (i in ACTION_OBJECTS.indices) {
                rules.add(ArrayList())
            }
            for (newRule in newRules) {
                if (newRule.content.trig.triggerType == "LocationTrigger") {
                    requestLocation = true
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
                        gson.fromJson(newRule.content.act.actionObject, VolumeAction::class.java)
                    }
                    else -> {
                        Log.d("Database-Error", "Unsupported Action Type in Database")
                        continue
                    }
                }
                val trig: TriggerObject = when (newRule.content.trig.triggerType) {
                    "TimeTrigger" -> {
                        gson.fromJson(newRule.content.trig.triggerObject, TimeTrigger::class.java)
                    }
                    "LocationTrigger" -> {
                        gson.fromJson(
                            newRule.content.trig.triggerObject,
                            LocationTrigger::class.java
                        )
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
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun showNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val replyIntent = Intent(this, AunoaService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_REPLY)
        }
        val achieveIntent = Intent(this, AunoaService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_ACHIEVE)
        }
        val replyPendingIntent = PendingIntent.getService(
            this, CODE_REPLY_INTENT, replyIntent, 0
        )
        val achievePendingIntent = PendingIntent.getService(
            this, CODE_ACHIEVE_INTENT, achieveIntent, 0
        )

        try {
            with(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_GENERAL,
                    "Aunoa",
                    NotificationManager.IMPORTANCE_LOW
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
            setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            priority = Notification.PRIORITY_LOW
            setContentIntent(replyPendingIntent)
            addAction(
                0, "REPLY", replyPendingIntent
            )
            addAction(
                0, "ACHIEVE", replyPendingIntent
            )
            startForeground(CODE_FOREGROUND_SERVICE, build())
        }
    }
}