package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow

class GetTags(
    private val repository: RuleRepository
)  {
    operator fun invoke(): Flow<List<Tag>> {
        return repository.getTags()
    }
}