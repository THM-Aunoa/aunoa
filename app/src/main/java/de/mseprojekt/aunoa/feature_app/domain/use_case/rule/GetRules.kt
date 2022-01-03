package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow

class GetRules(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(): Flow<List<RuleWithActAndTrig>> {
        return repository.getRules()
    }
}