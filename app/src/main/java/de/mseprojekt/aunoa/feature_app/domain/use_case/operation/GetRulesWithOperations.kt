package de.mseprojekt.aunoa.feature_app.domain.use_case.operation

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import kotlinx.coroutines.flow.Flow

class GetRulesWithOperations (
    private val repository: OperationRepository
    ){
    operator fun invoke(): Flow<List<RuleWithOperations>> {
        return repository.getRulesWithOperations()
    }
}