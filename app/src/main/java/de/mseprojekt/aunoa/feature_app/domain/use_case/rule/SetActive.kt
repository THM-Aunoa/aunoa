package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class SetActive(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(active:Boolean, id: Int) {
        return repository.setActive(active, id)
    }
}