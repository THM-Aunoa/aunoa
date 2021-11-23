package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.Rule

interface RuleRepository {

    suspend fun getRuleById(id: Int): Rule?
}