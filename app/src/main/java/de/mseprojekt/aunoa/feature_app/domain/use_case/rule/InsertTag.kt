package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class InsertTag(
    private val repository: RuleRepository
)  {
    operator fun invoke(title: String): Tag {
        repository.insertTag(Tag(title = title))
        return repository.getTagByName(title)
    }
}