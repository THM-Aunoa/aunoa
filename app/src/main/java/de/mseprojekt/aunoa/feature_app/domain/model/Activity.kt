package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Activity(
    @PrimaryKey
    val ActivityId: Int? = null,

    val date: Long,
    val RuleId: Int,
    val status: StatusType
)
