package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class GetRegionIdForCellId(
    private val repository: CellRepository
) {
    operator fun invoke(cellId: Long): Int? {
        return repository.getRegionIdForCellId(cellId)
    }
}