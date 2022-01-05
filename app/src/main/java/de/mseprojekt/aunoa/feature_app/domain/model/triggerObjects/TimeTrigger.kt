package de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects

data class TimeTrigger(
    val startTime : Long,
    val endTime : Long,
): TriggerObject()
