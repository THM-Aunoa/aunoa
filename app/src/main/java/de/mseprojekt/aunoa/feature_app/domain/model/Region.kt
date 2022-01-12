package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Region(
    @PrimaryKey
    val regionId: Int? = null,

    val name : String
)
