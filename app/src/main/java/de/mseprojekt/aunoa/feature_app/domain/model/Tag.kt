package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Tag(
    @PrimaryKey
    val tagId: Int? = null,

    val title: String,
    val description: String,
)
