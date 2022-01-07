package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class SetEnabled(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(enabled: Boolean, id: Int) {
        return repository.setEnabled(enabled, id)
    }
}