package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow

class GetRulesWithTags (
    private val repository: RuleRepository
) {
    operator fun invoke(): Flow<List<RuleWithTags>> {
        return repository.getRulesWithTags()
    }
}