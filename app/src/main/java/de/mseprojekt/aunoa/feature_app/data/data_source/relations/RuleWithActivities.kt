package de.mseprojekt.aunoa.feature_app.data.data_source.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.mseprojekt.aunoa.feature_app.domain.model.Activity
import de.mseprojekt.aunoa.feature_app.domain.model.Rule

data class RuleWithActivities(
    @Embedded val rule: Rule,
    @Relation(
        parentColumn = "RuleId",
        entityColumn = "RuleId"
    )
    val activities: List<Activity>
    )
