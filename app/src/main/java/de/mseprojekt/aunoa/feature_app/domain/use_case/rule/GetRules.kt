package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow

class GetRules(
    private val repository: RuleRepository
) {
    suspend operator fun invoke(): Flow<List<Rule>> {
        return repository.getRules()
    }
}