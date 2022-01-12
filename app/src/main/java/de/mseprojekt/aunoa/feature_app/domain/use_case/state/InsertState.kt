package de.mseprojekt.aunoa.feature_app.domain.use_case.state

import de.mseprojekt.aunoa.feature_app.domain.model.State
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository

class InsertState(
    private val repository: StateRepository
) {
    suspend operator fun invoke(state: State) {
        return repository.insertState(state)
    }
}