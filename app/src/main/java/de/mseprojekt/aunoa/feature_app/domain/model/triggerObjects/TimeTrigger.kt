package de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects

import java.time.DayOfWeek

data class TimeTrigger(
    val startTime : Int,
    val endTime : Int,
    val startWeekday : DayOfWeek?,
    val endWeekday: DayOfWeek?
): TriggerObject()
