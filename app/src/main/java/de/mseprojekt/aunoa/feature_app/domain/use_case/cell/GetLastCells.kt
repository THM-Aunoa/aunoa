package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.LastCellsWithRegion
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class GetLastCells(
    private val repository: CellRepository
) {
    @Synchronized
    operator fun invoke(): List<LastCellsWithRegion> {
        val reworkedCellList = ArrayList<LastCellsWithRegion>()
        val date = LocalDateTime.now().minusWeeks(1).toEpochSecond(ZoneOffset.UTC)
        repository.deleteLastCells(date)
        val lastCells = repository.getLastCells()
        val regions = repository.getRegions()
        val cells = repository.getAllCells()
        for (cell in lastCells){
            val foundCell = cells.map { c -> c.cellId }.indexOf(cell.cellId)
            if(foundCell != -1){
                reworkedCellList.add(
                    LastCellsWithRegion(
                    cellId = cell.cellId,
                    date = cell.date,
                    regionId = cells[foundCell].regionId,
                    regionName = regions[regions.map { r -> r.regionId }.indexOf(cells[foundCell].regionId)].name
                )
                )
            }else{
                reworkedCellList.add(
                    LastCellsWithRegion(
                        cellId = cell.cellId,
                        date = cell.date,
                    ))
            }
        }
        return reworkedCellList
    }
}