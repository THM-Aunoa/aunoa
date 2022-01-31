package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface RuleRepository {

    suspend fun getMaxIdFromRules(): Int?
    suspend fun getRuleById(id: Int): RuleWithActAndTrig?
    fun getRulesWithoutFlow(): List<RuleWithActAndTrig>
    fun getRulesWithTagsWithoutFlow(): List<RuleWithTags>
    fun getRules(): Flow<List<RuleWithActAndTrig>>
    fun getRulesWithTags(): Flow<List<RuleWithTags>>
    suspend fun getRuleWithTags(id: Int): RuleWithTags
    suspend fun insertRule(rule: Rule)
    suspend fun insertAction(action: Act)
    fun deleteRule(ruleId: Int)
    suspend fun insertTrigger(trigger: Trig)
    suspend fun setActive(active: Boolean, id: Int)
    suspend fun setEnabled(enabled: Boolean, id: Int)
    fun insertTag(tag: Tag)
    suspend fun clearTagsForRule(ruleId: Int)
    fun insertTags(tags: List<Tag>): List<Tag>
    suspend fun insertRuleTagCrossRef(ruleTagCrossRef : RuleTagCrossRef)
    fun getTags(): Flow<List<Tag>>
    //suspend fun getTagsForRule(id: Int): List<Tag>
    fun getTagsWithoutFlow(): List<Tag>
    fun getTagByName(title : String): Tag
}