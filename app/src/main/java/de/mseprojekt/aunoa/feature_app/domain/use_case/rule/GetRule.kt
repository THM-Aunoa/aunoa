package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class GetRule(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(id: Int): RuleWithActAndTrig? {
        return repository.getRuleById(id)
    }
}