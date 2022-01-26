package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow

class GetRulesWithTagsWithoutFlow (
    private val repository: RuleRepository
) {
    operator fun invoke(): List<RuleWithTags> {
        return repository.getRulesWithTagsWithoutFlow()
    }
}