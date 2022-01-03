package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.OperationsDao
import de.mseprojekt.aunoa.feature_app.domain.model.Operations
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationsRepository

class OperationsRepositoryImpl(
    private val dao: OperationsDao
): OperationsRepository {
    override suspend fun getOperationsById(id: Int): List<Operations> {
        return dao.getOperationsById(id)
    }

}