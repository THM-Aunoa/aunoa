package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class RemoveCell(
    private val repository: CellRepository
)  {
    suspend operator fun invoke(cellId: Long) {
        return repository.deleteCell(cellId)
    }
}