package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.*
import de.mseprojekt.aunoa.feature_app.domain.model.Cell
import de.mseprojekt.aunoa.feature_app.domain.model.LastCells
import de.mseprojekt.aunoa.feature_app.domain.model.Region
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCell(cell : Cell)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegion(region : Region)

    @Query("DELETE from cell WHERE cellId = :cellId")
    suspend fun deleteCell(cellId: Long)

    @Transaction
    fun deleteRegion(regionId: Int){
        deleteMainRegion(regionId)
        deleteCellsFromRegion(regionId)
    }

    @Query("DELETE from region WHERE regionId = :regionId")
    fun deleteMainRegion(regionId: Int)

    @Query("DELETE from cell WHERE regionId = :regionId")
    fun deleteCellsFromRegion(regionId: Int)

    @Query("SELECT cellId FROM cell, region WHERE name = :regionName and cell.regionId = region.regionId")
    fun getCellIdsByRegion(regionName: String): List<Long>

    @Query("SELECT * FROM region")
    fun getRegions(): List<Region>

    @Query("SELECT regionId FROM region WHERE name = :name")
    fun getRegionIdByName(name: String) : Int?

    @Query("SELECT name FROM region WHERE regionId = :id")
    fun getRegionNameById(id: Int) : String?

    @Query("SELECT * FROM lastcells")
    fun getLastCells(): Flow<List<LastCells>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastCell(last: LastCells)

    @Query("SELECT regionId FROM cell where cellId = :cellId")
    fun getRegionIdForCellId(cellId: Long): Int?
}