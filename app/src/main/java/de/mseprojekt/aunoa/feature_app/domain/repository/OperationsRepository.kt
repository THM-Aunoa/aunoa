package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.Operations

interface OperationsRepository {

    suspend fun getOperationsById(id: Int): List<Operations>
}