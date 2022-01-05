package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rule(
    @PrimaryKey
    val ruleId: Int? = null,
    val title: String,
    val description: String,
    val priority: Int,
    val active: Boolean,
    val enabled: Boolean
)
