package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import com.google.gson.Gson
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class InsertRule(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(
        action: ActionObject,
        actionObjectName: String,
        trigger: TriggerObject,
        triggerObjectName: String,
        title: String,
        description: String,
        priority: Int
    ) {
        var maxId = repository.getMaxIdFromRules()
        val gson = Gson()
        if (maxId == null)
            maxId = 0
        repository.insertRule(
            Rule(
                ruleId = maxId + 1,
                title = title,
                priority = priority,
                description = description,
                active = false,
                enabled = true
            )
        )
        val actionString = gson.toJson(action)
        repository.insertAction(
            Act(
                ruleId = maxId + 1,
                actionType = actionObjectName,
                actionObject = actionString
            )
        )
        val triggerString = gson.toJson(trigger)
        repository.insertTrigger(
            Trig(
                ruleId = maxId + 1,
                triggerType = triggerObjectName,
                triggerObject = triggerString
            )
        )
    }
}