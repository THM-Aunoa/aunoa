package de.mseprojekt.aunoa.feature_app.domain.use_case.state

import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository

class IsFirstRun(
    private val repository: StateRepository
) {
    suspend operator fun invoke(): Boolean {
        repository.getCurrentState() ?: return true
        return false
    }
}