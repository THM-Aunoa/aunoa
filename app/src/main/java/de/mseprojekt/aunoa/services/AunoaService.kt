package de.mseprojekt.aunoa.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
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
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
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
import com.google.android.gms.tasks.Tasks





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
    private var rules: ArrayList<ArrayList<RuleWithActAndTrig>> = ArrayList()
    @Inject
    lateinit var ruleUseCases: RuleUseCases

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
                endTime = LocalTime.now().toSecondOfDay()+120,
                startTime = LocalTime.now().toSecondOfDay()-120,
                endWeekday = LocalDate.now().dayOfWeek,
                startWeekday = LocalDate.now().dayOfWeek
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
            updateRuleList()
            Log.d("a", rules.toString())
            while (isrunning) {
                for (ruleCategories in rules){
                    for(rule in ruleCategories){
                        if (rule.rule.enabled){
                            if (rule.rule.active){
                                val result = testTrigger(rule.content.trig.triggerType, rule.content.trig.triggerObject,gson, true)
                                if (result){
                                    Log.d("Action", "Action will be performed")
                                    performAction(rule.content.act.actionType, rule.content.act.actionObject,gson, true)
                                } else {
                                    break
                                }
                            }else{
                                val result = testTrigger(rule.content.trig.triggerType, rule.content.trig.triggerObject,gson)
                                if (result){
                                    Log.d("Action", "Action will be performed")
                                    performAction(rule.content.act.actionType, rule.content.act.actionObject,gson)
                                    break
                                }
                            }
                        }
                    }
                }
                delay(delay)
            }
        }
    }

    private fun testTrigger(triggerType: String, triggerString: String, gson: Gson, reverse: Boolean = false): Boolean{
        when(triggerType){
            "TimeTrigger" -> {
                val timeTrigger = gson.fromJson(triggerString, TimeTrigger::class.java)
                val weekday = if (reverse) timeTrigger.endWeekday else timeTrigger.startWeekday
                if (weekday == LocalDate.now().dayOfWeek){
                    val time = if (reverse) timeTrigger.endTime else timeTrigger.startTime
                    if (LocalTime.now().toSecondOfDay() > time) {
                        return true
                    }
                }
                return false
            }
            "LocationTrigger" -> {
                val locationTrigger = gson.fromJson(triggerString, LocationTrigger::class.java)
                val task = requestCurrentLocation()
                val result = Tasks.await(task)
                return if (result != null) {
                    Log.d("Cord Received", result.latitude.toString())
                    Log.d("Cord Received", result.longitude.toString())
                    val distance = distanceBetweenTwoPoints(
                        result.latitude,
                        result.longitude,
                        locationTrigger.latitude,
                        locationTrigger.longitude
                    )
                    if (reverse) {
                        distance > locationTrigger.radius
                    } else {
                        locationTrigger.radius >= distance
                    }
                } else {
                    false
                }
            }
            else -> {
                Log.d("Database-Error", "Unsupported Triggered Type in Database")
                return false
            }
        }
    }


    private fun performAction(actionType: String, actionString: String,gson: Gson, reverse: Boolean = false){
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when(actionType){
            "VolumeAction" -> {
                if (manager.isNotificationPolicyAccessGranted) {
                    val volumeAction = gson.fromJson(actionString, VolumeAction::class.java)
                    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    when (if (reverse) volumeAction.deactivateVolume else volumeAction.activateVolume) {
                        0 -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                        1 -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                        2 -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    }
                }

            }
        }
    }

    fun updateRuleList(){
        // Todo this block + testTrigger/action needs to be in a mutex
        val newRules = ruleUseCases.getRulesWithoutFlow()
        rules = ArrayList()
        for (i in ACTION_OBJECTS.indices){
            rules.add(ArrayList())
        }
        for (newRule in newRules) {
            var index = -1
            for (i in ACTION_OBJECTS.indices) {
                if (ACTION_OBJECTS[i] == newRule.content.act.actionType){
                    index = i
                }
            }
            if (index == -1) {
                Log.d("Database-Error", "Unsupported Action Type in Database")
                return
            }
            if (rules[index].size == 0){
                rules[index].add(newRule)
                continue
            }
            if (newRule.rule.priority <= rules[index][rules[index].size-1].rule.priority){
                rules[index].add(rules[index].size,newRule)
                continue
            }
            for (i in 0 until rules[index].size){
                if (newRule.rule.priority > rules[index][i].rule.priority){
                    rules[index].add(i, newRule)
                    break
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