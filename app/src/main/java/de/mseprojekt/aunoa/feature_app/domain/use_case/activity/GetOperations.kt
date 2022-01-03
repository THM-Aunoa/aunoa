package de.mseprojekt.aunoa.feature_app.domain.use_case.activity

import de.mseprojekt.aunoa.feature_app.domain.model.Operations
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationsRepository

class GetOperations (
    private val repository: OperationsRepository
    ){
    suspend operator fun invoke(id: Int): List<Operations> {
        return repository.getOperationsById(id)

    }
}