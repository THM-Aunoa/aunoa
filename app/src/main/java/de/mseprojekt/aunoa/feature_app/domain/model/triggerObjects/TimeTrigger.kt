package de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects

import java.time.DayOfWeek

data class TimeTrigger(
    val startTime : Long,
    val endTime : Long,
    val startWeekday : DayOfWeek?,
    val endWeekday: DayOfWeek?
): TriggerObject()
