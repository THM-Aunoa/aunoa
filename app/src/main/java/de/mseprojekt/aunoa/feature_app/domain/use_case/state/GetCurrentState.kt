package de.mseprojekt.aunoa.feature_app.domain.use_case.state

import de.mseprojekt.aunoa.feature_app.domain.model.State
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository

class GetCurrentState(
    private val repository: StateRepository
) {
    suspend operator fun invoke(): Boolean {
        val state = repository.getCurrentState() ?: return true
        return state.status
    }
}