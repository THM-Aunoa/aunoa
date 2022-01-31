package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastCells(
    @PrimaryKey
    val cellId: Long,

    val regionId: Int? = null,

    val date: Long,

)
