package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cell(
    @PrimaryKey
    val cellId: Long,

    val regionId : Int,
)
