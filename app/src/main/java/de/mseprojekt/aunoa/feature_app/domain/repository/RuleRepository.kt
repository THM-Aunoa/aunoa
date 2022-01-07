package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig
import kotlinx.coroutines.flow.Flow

interface RuleRepository {

    suspend fun getMaxIdFromRules(): Int?
    suspend fun getRuleById(id: Int): RuleWithActAndTrig?
    fun getRulesWithoutFlow(): List<RuleWithActAndTrig>
    fun getRules(): Flow<List<RuleWithActAndTrig>>
    fun getRulesWithTags(): Flow<List<RuleWithTags>>
    suspend fun insertRule(rule: Rule)
    suspend fun insertAction(action: Act)
    suspend fun insertTrigger(trigger: Trig)
    suspend fun setActive(active: Boolean, id: Int)
    suspend fun setEnabled(enabled: Boolean, id: Int)
}