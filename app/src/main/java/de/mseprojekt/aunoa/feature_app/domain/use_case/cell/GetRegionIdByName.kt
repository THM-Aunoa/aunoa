package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class GetRegionIdByName(
    private val repository: CellRepository
) {
    operator fun invoke(name : String): Int? {
        return repository.getRegionIdByName(name)
    }
}