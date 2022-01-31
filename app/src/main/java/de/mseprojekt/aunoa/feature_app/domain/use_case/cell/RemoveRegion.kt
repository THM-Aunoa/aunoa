package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class RemoveRegion(
    private val repository: CellRepository
)  {
    operator fun invoke(regionId: Int) {
        return repository.deleteRegion(regionId)
    }
}