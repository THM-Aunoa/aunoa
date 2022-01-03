package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Operations(
    @PrimaryKey
    val operationId: Int? = null,

    val date: Long,
    val ruleId: Int,
    val status: StatusType
)
