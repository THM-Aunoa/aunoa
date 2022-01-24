package de.mseprojekt.aunoa.feature_app.domain.use_case.state

import de.mseprojekt.aunoa.feature_app.domain.model.State
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class InsertState(
    private val repository: StateRepository
) {
    suspend operator fun invoke(state: Boolean) {
        return repository.insertState(State(
            status = state,
            date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        ))
    }
}