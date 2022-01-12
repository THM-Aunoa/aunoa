package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.mseprojekt.aunoa.feature_app.domain.model.Cell
import de.mseprojekt.aunoa.feature_app.domain.model.Region

@Dao
interface CellDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCell(cell : Cell)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegion(region : Region)

    @Query("DELETE from cell WHERE cellId = :cellId")
    suspend fun deleteCell(cellId: Long)

    @Query("DELETE from region WHERE regionId = :regionId")
    suspend fun deleteRegion(regionId: Int)

    @Query("SELECT cellId FROM cell, region WHERE name = :regionName and cell.regionId = region.regionId")
    fun getCellIdsByRegion(regionName: String): List<Long>

    @Query("SELECT * FROM region")
    fun getRegions(): List<Region>
}