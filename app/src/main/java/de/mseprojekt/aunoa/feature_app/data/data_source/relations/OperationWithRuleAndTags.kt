package de.mseprojekt.aunoa.feature_app.data.data_source.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.model.Rule

data class OperationWithRuleAndTags(
    @Embedded val operation: Operation,
    @Relation(
        entity = Rule::class,
        parentColumn = "ruleId",
        entityColumn = "ruleId"
    )
    val ruleWithTags: RuleWithTags,
)
