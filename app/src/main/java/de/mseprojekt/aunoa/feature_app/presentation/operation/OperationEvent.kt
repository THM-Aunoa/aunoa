package de.mseprojekt.aunoa.feature_app.presentation.operation

import de.mseprojekt.aunoa.other.AunoaEventInterface

sealed class OperationEvent : AunoaEventInterface{
    data class EnteredUsername(val value: String): OperationEvent()
    data class EnteredEmail(val value: String): OperationEvent()
    data class ToggleAppState(val value: Boolean): OperationEvent()
    data class DeleteRegion(val value: Int): OperationEvent()
    data class AddRegion(val value: String, val minutes: Long): OperationEvent()
    data class EditRegion(val id: Int, val value: String, val minutes: Long): OperationEvent()
    object SaveUserDetails: OperationEvent()
}