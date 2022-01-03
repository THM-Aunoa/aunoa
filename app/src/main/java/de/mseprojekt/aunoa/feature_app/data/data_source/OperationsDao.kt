package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import de.mseprojekt.aunoa.feature_app.domain.model.Operations
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationsDao {

    @Query("SELECT * FROM operations")
    fun getOperations(): Flow<List<Operations>>

    @Transaction
    @Query("SELECT * FROM operations WHERE ruleId = :id")
    suspend fun getOperationsById(id: Int): List<Operations>
}