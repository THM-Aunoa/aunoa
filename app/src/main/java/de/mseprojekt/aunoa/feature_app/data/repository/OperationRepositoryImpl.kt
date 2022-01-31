package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.OperationDao
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.OperationWithRuleAndTags
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import kotlinx.coroutines.flow.Flow

class OperationRepositoryImpl(
    private val dao: OperationDao
): OperationRepository {
    override suspend fun getOperationsById(id: Int): List<Operation> {
        return dao.getOperationsById(id)
    }

    override fun getOperationsWithRuleAndTags(): Flow<List<OperationWithRuleAndTags>> {
        return dao.getOperationsWithRuleAndTags()
    }

    override fun getRulesWithOperations(): Flow<List<RuleWithOperations>> {
        return dao.getRulesWithOperations()
    }

    override suspend fun insertOperation(operation: Operation) {
        return dao.insertOperation(operation)
    }

}