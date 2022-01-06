package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class GetRulesWithoutFlow (
    private val repository: RuleRepository
) {
    operator fun invoke(): List<RuleWithActAndTrig> {
        return repository.getRulesWithoutFlow()
    }
}