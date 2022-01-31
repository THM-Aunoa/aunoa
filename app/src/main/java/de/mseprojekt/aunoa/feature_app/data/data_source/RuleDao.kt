package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.appcompat.widget.DialogTitle
import androidx.room.*
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {

    @Query("SELECT MAX(RuleId) FROM rule")
    suspend fun getMaxIdFromRules(): Int?

    @Transaction
    @Query("SELECT * FROM rule")
    fun getRulesWithTags(): Flow<List<RuleWithTags>>

    @Transaction
    @Query("SELECT * FROM rule where ruleId=:id")
    fun getRuleWithTags(id: Int): RuleWithTags

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuleTagCrossRef(ruleTagCrossRef : RuleTagCrossRef)

    @Query("SELECT * FROM tag")
    fun getTags(): Flow<List<Tag>>

    /*@Query("SELECT * FROM tag")
    suspend fun getTagsForRule(id: Int): List<Tag>*/

    @Query("SELECT * FROM tag where title= :title")
    fun getTagByName(title: String): Tag

    @Query("SELECT * FROM rule")
    fun getTagsWithoutFlow(): List<Tag>


}