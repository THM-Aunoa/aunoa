package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.StateDao
import de.mseprojekt.aunoa.feature_app.domain.model.State
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository

class StateRepositoryImpl(
    private val stateDao: StateDao
): StateRepository {
    override suspend fun getCurrentState(): State? {
        return stateDao.getCurrentState()
    }
    override suspend fun insertState(state: State) {
        stateDao.insertState(state)
    }
}