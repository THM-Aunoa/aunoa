package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.*
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {

    @Query("SELECT MAX(RuleId) FROM rule")
    suspend fun getMaxIdFromRules(): Int?

    @Transaction
    @Query("SELECT * FROM rule")
    fun getRules(): Flow<List<RuleWithActAndTrig>>

    @Transaction
    @Query("SELECT * FROM rule WHERE RuleId = :id")
    suspend fun getRuleById(id: Int): RuleWithActAndTrig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: Rule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: Act)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrigger(trigger: Trig)
}