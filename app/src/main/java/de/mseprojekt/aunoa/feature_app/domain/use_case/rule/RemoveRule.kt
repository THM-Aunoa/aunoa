package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class RemoveRule(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(ruleId: Int) {
        return repository.deleteRule(ruleId)
    }
}