package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class InsertCell(
    private val repository: CellRepository
)  {
    suspend operator fun invoke(regionId: Int,cellId: Long) {
        return repository.insertCell(regionId, cellId)
    }
}