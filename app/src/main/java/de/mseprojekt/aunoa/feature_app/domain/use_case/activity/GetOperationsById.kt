package de.mseprojekt.aunoa.feature_app.domain.use_case.activity

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository

class GetOperationsById (
    private val repository: OperationRepository
){
    suspend operator fun invoke(id: Int): List<Operation> {
        return repository.getOperationsById(id)

    }
}