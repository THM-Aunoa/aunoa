package de.mseprojekt.aunoa.feature_app.domain.use_case.activity

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import kotlinx.coroutines.flow.Flow

class GetOperations (
    private val repository: OperationRepository
    ){
    operator fun invoke(): Flow<List<RuleWithOperations>> {
        return repository.getOperations()

    }
}