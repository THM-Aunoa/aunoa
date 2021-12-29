package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {

    @Query("SELECT * FROM rule")
    fun getRules(): Flow<List<Rule>>

    @Query("SELECT * FROM rule WHERE RuleId = :id")
    suspend fun getRuleById(id: Int): Rule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: Rule)
}