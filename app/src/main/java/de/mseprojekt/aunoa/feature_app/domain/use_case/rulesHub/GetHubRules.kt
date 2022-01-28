package de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub

import android.util.Log
import com.google.gson.Gson
import de.mseprojekt.aunoa.feature_app.data.remote.RulesHubAPI
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRule
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.SpotifyAction
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.*
import org.json.JSONArray
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import org.json.JSONObject
import java.lang.Exception
import java.time.DayOfWeek


class GetHubRules(
    private val api: RulesHubAPI
) {
    operator fun invoke(): ArrayList<UnzippedRuleWithTags> {
        val result: ArrayList<UnzippedRuleWithTags> = ArrayList()
        try {
            val callable = Callable { api.getHubRules() }

            val future = Executors.newSingleThreadExecutor().submit(callable)

            val jsonString = future!!.get()
            if (jsonString!=null) {
                val rules: JSONArray = JSONObject(jsonString).get("rules") as JSONArray
                val gson = Gson()
                for(i in 0 until rules.length()){
                    val jsonRule = rules.get(i) as JSONObject
                    val innerRule = jsonRule.get("rule") as JSONObject
                    val rule = Rule(
                        ruleId = i,
                        title = innerRule.get("title") as String,
                        description = innerRule.get("description") as String,
                        priority = innerRule.get("priority") as Int,
                        active = innerRule.get("active") as Boolean,
                        enabled = innerRule.get("enabled") as Boolean
                    )
                    val action = when(jsonRule.get("actionObjectName")){
                        "VolumeAction" -> {
                            gson.fromJson(jsonRule.get("action").toString(), VolumeAction::class.java)
                        }
                        "SpotifyAction" ->{
                            gson.fromJson(jsonRule.get("action").toString(), SpotifyAction::class.java)
                        }
                        else -> {
                            Log.d("Database-Error", "Got Unsupported Action Type from API")
                            continue
                        }
                    }
                    val trigger = when(jsonRule.get("triggerObjectName")){
                        "TimeTrigger" -> {
                            val innerTrigger = jsonRule.get("trigger") as JSONObject
                            TimeTrigger(
                                startTime = innerTrigger.get("startTime") as Int,
                                endTime = innerTrigger.get("endTime") as Int,
                                startWeekday = stringToWeekday(innerTrigger.get("startWeekday") as String),
                                endWeekday = stringToWeekday(innerTrigger.get("endWeekday") as String)
                            )
                        }
                        "LocationTrigger" -> {
                            gson.fromJson(
                                jsonRule.get("trigger").toString(),
                                LocationTrigger::class.java
                            )
                        }
                        "WifiTrigger" ->{
                            gson.fromJson(
                                jsonRule.get("trigger").toString(),
                                WifiTrigger::class.java
                            )
                        }
                        "BluetoothTrigger" -> {
                            gson.fromJson(
                                jsonRule.get("trigger").toString(),
                                BluetoothTrigger::class.java
                            )
                        }
                        "NfcTrigger" -> {
                            gson.fromJson(
                                jsonRule.get("trigger").toString(),
                                NfcTrigger::class.java
                            )
                        }
                        "CellTrigger" -> {
                            gson.fromJson(
                                jsonRule.get("trigger").toString(),
                                CellTrigger::class.java
                            )
                        }
                        else -> {
                            Log.d("Database-Error", "Got Unsupported Action Type from API")
                            continue
                        }
                    }
                    val tags: JSONArray = jsonRule.get("tags") as JSONArray
                    val tagList: ArrayList<Tag> = ArrayList()
                    for(j in 0 until tags.length()) {
                        val tag = tags.get(j) as JSONObject
                        tagList.add(Tag(
                            title = tag.get("title") as String
                        ))
                    }
                    result.add(
                        UnzippedRuleWithTags(
                            rule = rule,
                            action = action,
                            trigger = trigger,
                            tags = tagList
                        )
                    )
                }
            }
        } catch (ex: Exception){
            Log.d("Network", "Network Exception happened")
        }
        return result
    }


    private fun stringToWeekday(weekday: String): DayOfWeek? {
        return when(weekday){
            "Monday" ->{
                DayOfWeek.MONDAY
            }
            "Tuesday" ->{
                DayOfWeek.THURSDAY
            }
            "Wednesday" ->{
                DayOfWeek.WEDNESDAY
            }
            "Thursday" ->{
                DayOfWeek.THURSDAY
            }
            "Friday" ->{
                DayOfWeek.FRIDAY
            }
            "Saturday" ->{
                DayOfWeek.SATURDAY
            }
            "Sunday" ->{
                DayOfWeek.SUNDAY
            }
            else ->{
                null
            }
        }

    }
}