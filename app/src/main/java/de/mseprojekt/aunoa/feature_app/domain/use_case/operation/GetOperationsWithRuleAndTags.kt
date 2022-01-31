package de.mseprojekt.aunoa.feature_app.domain.use_case.operation

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.OperationWithRuleAndTags
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import kotlinx.coroutines.flow.Flow

class GetOperationsWithRuleAndTags (
    private val repository: OperationRepository
    ){
    operator fun invoke(): Flow<List<OperationWithRuleAndTags>> {
        return repository.getOperationsWithRuleAndTags()

    }
}