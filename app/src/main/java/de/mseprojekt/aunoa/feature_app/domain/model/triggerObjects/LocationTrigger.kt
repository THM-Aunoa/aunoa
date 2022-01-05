package de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects

data class LocationTrigger(
    val latitude: Double,
    val longitude: Double,
    val radius: Double
): TriggerObject()
