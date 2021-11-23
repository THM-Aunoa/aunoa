package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VolumeAction(
    override val name : String = "Volume Change",
    val volume: Int,

    @PrimaryKey
    override val actionId: Int
): Action()