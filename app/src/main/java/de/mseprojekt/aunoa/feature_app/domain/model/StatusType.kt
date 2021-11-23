package de.mseprojekt.aunoa.feature_app.domain.model

sealed class StatusType {
    object Success: StatusType()
    object Failure: StatusType()
}