package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class EditRegion(
    private val repository: CellRepository
)  {
    suspend operator fun invoke(regionId: Int, regionName: String) {
        repository.insertRegion(Region(regionId = regionId, name= regionName))
    }
}