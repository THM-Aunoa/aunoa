package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeTrigger(
    override val name: String = "Time Trigger",
    val startTime : Long,
    val endTime : Long,

    @PrimaryKey
    override val actionId: Int

): Trigger()