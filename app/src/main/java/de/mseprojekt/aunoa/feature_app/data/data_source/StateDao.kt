package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.*
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.model.State
import de.mseprojekt.aunoa.feature_app.domain.model.Trig


@Dao
interface StateDao {

    @Transaction
    @Query("SELECT * FROM state WHERE stateId = (SELECT MAX(stateId) FROM state)")
    suspend fun getCurrentState(): State?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: State)

}