package de.mseprojekt.aunoa.feature_app.data.data_source.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.RuleTagCrossRef
import de.mseprojekt.aunoa.feature_app.domain.model.Tag

data class RuleWithTags(
    @Embedded val rule: Rule,
    @Relation(
        parentColumn = "ruleId",
        entityColumn = "tagId",
        associateBy = Junction(RuleTagCrossRef::class)
    )
    val tags: List<Tag>
)
