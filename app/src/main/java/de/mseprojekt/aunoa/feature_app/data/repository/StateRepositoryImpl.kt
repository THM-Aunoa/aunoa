package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.StateDao
import de.mseprojekt.aunoa.feature_app.domain.model.State
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class StateRepositoryImpl(
    private val stateDao: StateDao
): StateRepository {
    override fun getCurrentState(): State? {
        val callable = Callable{ stateDao.getCurrentState() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }
    override suspend fun insertState(state: State) {
        stateDao.insertState(state)
    }
}