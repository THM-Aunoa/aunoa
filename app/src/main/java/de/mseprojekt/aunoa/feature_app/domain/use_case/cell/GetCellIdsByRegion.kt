package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class GetCellIdsByRegion(
    private val repository: CellRepository
) {
    operator fun invoke(name: String): List<Long> {
        return repository.getCellIdsByRegion(name)
    }
}