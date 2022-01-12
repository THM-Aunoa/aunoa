package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.Region


interface CellRepository {

    fun getCellIdsByRegion(name: String): List<Long>
    suspend fun insertRegion(name: String)
    suspend fun deleteRegion(id: Int)
    suspend fun insertCell(regionId: Int, cellId: Long)
    suspend fun deleteCell(cellId: Long)
    fun getRegions(): List<Region>
}