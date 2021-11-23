package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class GetRule(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(id: Int): Rule? {
        return repository.getRuleById(id)
    }
}