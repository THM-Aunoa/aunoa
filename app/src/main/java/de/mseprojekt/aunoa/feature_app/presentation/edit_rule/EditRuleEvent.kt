package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

sealed class EditRuleEvent {
    object SaveRule: EditRuleEvent()
    object DeleteRule: EditRuleEvent()
    object RemoveTrigger: EditRuleEvent()
    object FillCurrentLocationBoxes: EditRuleEvent()

    data class EnteredTitle(val value: String): EditRuleEvent()
    data class EnteredDescription(val value: String): EditRuleEvent()
    data class EnteredPriority(val value: Int): EditRuleEvent()

    data class EnteredStartDay(val value: String): EditRuleEvent()
    data class EnteredStartTimeHour(val value: String): EditRuleEvent()
    data class EnteredStartTimeMinutes(val value: String): EditRuleEvent()
    data class EnteredEndDay(val value: String): EditRuleEvent()
    data class EnteredEndTimeHour(val value: String): EditRuleEvent()
    data class EnteredEndTimeMinutes(val value: String): EditRuleEvent()

    data class ChoosedTrigger(val value: String): EditRuleEvent()
    data class ChoosedRegion(val value: String): EditRuleEvent()
    data class RemoveTag(val value: Int): EditRuleEvent()
    data class AddTag(val value: String): EditRuleEvent()

    data class EnteredLatitude(val value: String): EditRuleEvent()
    data class EnteredLongitude(val value: String): EditRuleEvent()
    data class EnteredRadius(val value: Int): EditRuleEvent()
}