package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class InsertRegion(
    private val repository: CellRepository
) {
    operator fun invoke(regionName: String, scanUntil: Long, minutes: Long = 0) {
        if (repository.getRegionIdByName(regionName) == null) {
            if(minutes > 0){
                repository.cleanRegions()
            }
            repository.insertRegion(Region(name = regionName, scanUntil = scanUntil))
        }
    }
}