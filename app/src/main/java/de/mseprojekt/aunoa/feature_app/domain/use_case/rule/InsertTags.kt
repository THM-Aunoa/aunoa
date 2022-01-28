package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class InsertTags(
    private val repository: RuleRepository
)  {
    operator fun invoke(tags : List<Tag>): List<Tag> {
        return repository.insertTags(tags)
    }
}