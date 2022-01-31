package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import com.google.gson.Gson
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.SpotifyAction
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.*
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class InsertRule(
    private val repository: RuleRepository
) {
    @Synchronized
    suspend operator fun invoke(
        action: ActionObject,
        trigger: TriggerObject,
        title: String,
        description: String,
        priority: Int,
        id: Int? = -1
    ) : Int {
        val maxId = repository.getMaxIdFromRules()
        val gson = Gson()
        var ruleId = id
        if (ruleId == -1) {
            if (maxId == null) {
                ruleId = 1
            } else {
                ruleId = maxId + 1
            }
        }
        repository.insertRule(
            Rule(
                ruleId = ruleId,
                title = title,
                priority = priority,
                description = description,
                active = false,
                enabled = true
            )
        )

        val actionObjectName = when (action) {
            is VolumeAction -> {
                "VolumeAction"
            }
            is SpotifyAction ->{
                "SpotifyAction"
            }
            else -> {
                if (ruleId != null) {
                    repository.deleteRule(ruleId)
                }
                return -1
            }
        }

        val triggerObjectName = when (trigger) {
            is TimeTrigger -> {
                "TimeTrigger"
            }
            is LocationTrigger -> {
                "LocationTrigger"
            }
            is WifiTrigger -> {
                "WifiTrigger"
            }
            is BluetoothTrigger -> {
                "BluetoothTrigger"
            }
            is NfcTrigger -> {
                "NfcTrigger"
            }
            is CellTrigger -> {
                "CellTrigger"
            }
            else -> {
                if (ruleId != null) {
                    repository.deleteRule(ruleId)
                }
                return -1
            }
        }

        val actionString = gson.toJson(action)
        repository.insertAction(
            Act(
                ruleId = ruleId,
                actionType = actionObjectName,
                actionObject = actionString
            )
        )
        val triggerString = gson.toJson(trigger)
        repository.insertTrigger(
            Trig(
                ruleId = ruleId,
                triggerType = triggerObjectName,
                triggerObject = triggerString
            )
        )

        return ruleId!!
    }
}