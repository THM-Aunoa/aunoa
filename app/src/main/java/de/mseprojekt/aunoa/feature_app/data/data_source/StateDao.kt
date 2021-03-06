package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.*
import de.mseprojekt.aunoa.feature_app.domain.model.State


@Dao
interface StateDao {

    @Transaction
    @Query("SELECT * FROM state WHERE stateId = (SELECT MAX(stateId) FROM state)")
    fun getCurrentState(): State?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: State)

}