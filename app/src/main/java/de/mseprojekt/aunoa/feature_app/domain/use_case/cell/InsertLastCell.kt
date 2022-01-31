package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.LastCells
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class InsertLastCell(
    private val repository: CellRepository
) {
    operator fun invoke(cellId: Long, regionId: Int?) {
        return repository.insertLastCell(LastCells(cellId = cellId, date= LocalDateTime.now().toEpochSecond(
            ZoneOffset.UTC), regionId = regionId))
    }
}