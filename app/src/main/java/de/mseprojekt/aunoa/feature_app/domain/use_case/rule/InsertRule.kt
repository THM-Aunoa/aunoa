package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class InsertRule(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(action: Act, trigger: Trig, title: String, priority: Int) {
        var maxId = repository.getMaxIdFromRules()
        if (maxId == null)
            maxId = 0
        repository.insertRule(Rule(
            ruleId = maxId + 1,
            title = title,
            priority = priority,
            description = "test",
            active = true
            )
        )
        repository.insertAction(action.copy(
            ruleId = maxId + 1
        ))
        repository.insertTrigger(trigger.copy(
            ruleId = maxId + 1
        ))
    }
}