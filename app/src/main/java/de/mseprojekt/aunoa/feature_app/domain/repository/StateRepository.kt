package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.State

interface StateRepository {

    suspend fun getCurrentState(): State?
    suspend fun insertState(state: State)
}