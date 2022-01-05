package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.State
import kotlinx.coroutines.flow.Flow

interface StateRepository {

    suspend fun getCurrentState(): State?
    suspend fun insertState(state: State)
}