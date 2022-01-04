package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    suspend fun getOperationsById(id: Int): List<Operation>
    suspend fun insertOperation(operation: Operation)
    fun getOperations(): Flow<List<RuleWithOperations>>
}