package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.RuleTagCrossRef
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class InsertRuleTagCrossRef(
    private val repository: RuleRepository
)  {
    suspend operator fun invoke(ruleId: Int, tagId: Int) {
        repository.insertRuleTagCrossRef(RuleTagCrossRef(ruleId = ruleId, tagId = tagId))
    }
}