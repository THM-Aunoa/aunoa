package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Trig(

    @PrimaryKey
    val ruleId: Int? = null,

    val triggerType: String,

    val startTime : Long? = null,
    val endTime : Long? = null,

    val latitude: Long? = null,
    val longitude: Long? = null,
    val radius: Int? = null,

)