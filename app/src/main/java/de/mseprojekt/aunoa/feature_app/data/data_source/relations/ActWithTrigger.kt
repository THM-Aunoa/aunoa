package de.mseprojekt.aunoa.feature_app.data.data_source.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Trig

data class ActWithTrig(
    @Embedded val act: Act,
    @Relation(
        parentColumn = "ruleId",
        entityColumn = "ruleId",
    )
    val trig: Trig
)
