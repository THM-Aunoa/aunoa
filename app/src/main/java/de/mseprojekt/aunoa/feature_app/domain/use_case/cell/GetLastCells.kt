package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.LastCells
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository
import kotlinx.coroutines.flow.Flow

class GetLastCells(
    private val repository: CellRepository
) {
    operator fun invoke(): Flow<List<LastCells>> {
        return repository.getLastCells()
    }
}