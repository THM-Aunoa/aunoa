package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import kotlinx.coroutines.flow.Flow

interface RuleRepository {

    suspend fun getRuleById(id: Int): Rule?
    fun getRules(): Flow<List<Rule>>
    suspend fun insertRule(rule: Rule)
}