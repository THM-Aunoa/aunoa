package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class State(
    @PrimaryKey
    val stateId: Int? = null,

    val status: Boolean,
    val date: Long
)