package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["ruleId", "tagId"])
data class RuleTagCrossRef(
    val ruleId: Int,
    val tagId: Int
)
