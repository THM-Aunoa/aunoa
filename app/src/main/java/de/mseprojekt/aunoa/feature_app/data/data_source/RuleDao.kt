package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.*
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
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
    fun getRulesWithTags(): Flow<List<RuleWithTags>>

    @Transaction
    @Query("SELECT * FROM rule")
    fun getRules(): Flow<List<RuleWithActAndTrig>>

    @Transaction
    @Query("SELECT * FROM rule")
    fun getRulesWithoutFlow(): List<RuleWithActAndTrig>

    @Transaction
    @Query("SELECT * FROM rule")
    fun getRulesWithTagsWithoutFlow(): List<RuleWithTags>

    @Transaction
    @Query("SELECT * FROM rule WHERE ruleId = :id")
    suspend fun getRuleById(id: Int): RuleWithActAndTrig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: Rule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: Act)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrigger(trigger: Trig)

    @Query("Update rule SET active=:active WHERE ruleId = :id")
    suspend fun setActive(active: Boolean, id: Int)

    @Query("Update rule SET enabled=:enabled WHERE ruleId = :id")
    suspend fun setEnabled(enabled: Boolean, id: Int)

    @Query("DELETE from rule WHERE ruleId = :ruleId")
    suspend fun deleteRule(ruleId: Int)
}