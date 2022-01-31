package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class InsertRegion(
    private val repository: CellRepository
) {
    suspend operator fun invoke(regionName: String) {
        if (repository.getRegionIdByName(regionName) == null) {
            repository.insertRegion(Region(name = regionName))
        }
    }
}