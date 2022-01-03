package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Act (
    @PrimaryKey
    val ruleId: Int? = null,

    val actionType: String,

    val volume: Int? = null,
)