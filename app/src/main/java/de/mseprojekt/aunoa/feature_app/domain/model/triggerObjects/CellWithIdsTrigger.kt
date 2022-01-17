package de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects

data class CellWithIdsTrigger (
    val cellIds : MutableList<Long>,
    val id: Int
) : TriggerObject()