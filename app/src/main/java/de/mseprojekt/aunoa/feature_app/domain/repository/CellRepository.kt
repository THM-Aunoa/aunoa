package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.LastCells
import de.mseprojekt.aunoa.feature_app.domain.model.Region
import kotlinx.coroutines.flow.Flow

interface CellRepository {

    fun getCellIdsByRegion(name: String): List<Long>
    fun getRegionIdByName(name: String): Int?
    fun insertRegion(region : Region)
    fun deleteRegion(id: Int)
    fun insertCell(regionId: Int, cellId: Long)
    fun insertLastCell(last: LastCells)
    suspend fun deleteCell(cellId: Long)
    fun getRegions(): List<Region>
    fun getRegionNameById(id: Int): String?
    fun cleanRegions()
    fun getLastCells(): Flow<List<LastCells>>
    fun getRegionIdForCellId(cellId: Long): Int?
}