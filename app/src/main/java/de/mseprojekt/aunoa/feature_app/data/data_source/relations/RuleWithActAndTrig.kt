package de.mseprojekt.aunoa.feature_app.data.data_source.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule

data class RuleWithActAndTrig(
    @Embedded val rule: Rule,
    @Relation(
        entity = Act::class,
        parentColumn = "ruleId",
        entityColumn = "ruleId"
    )
    val content: ActWithTrig,
)

