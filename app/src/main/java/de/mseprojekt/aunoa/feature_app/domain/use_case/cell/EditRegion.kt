package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class EditRegion(
    private val repository: CellRepository
)  {
    operator fun invoke(regionId: Int, regionName: String, scanUntil: Long, minutes: Long = 0) {
        if(minutes > 0){
            repository.cleanRegions()
        }
        repository.insertRegion(Region(regionId = regionId, name= regionName, scanUntil = scanUntil))
    }
}