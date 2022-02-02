package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.Cell
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class GetAllCells(
    private val repository: CellRepository
) {
    operator fun invoke(): List<Cell> {
        return repository.getAllCells()
    }
}