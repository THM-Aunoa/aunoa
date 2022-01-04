package de.mseprojekt.aunoa.feature_app.domain.use_case.activity


import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository

class InsertOperation(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(operation : Operation) {
        return repository.insertOperation(operation)
    }
}
