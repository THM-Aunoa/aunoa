package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

sealed class EditRuleEvent {
    object SaveRule: EditRuleEvent()
    object DeleteRule: EditRuleEvent()
    object FillCurrentLocationBoxes: EditRuleEvent()
    data class EnteredTitle(val value: String): EditRuleEvent()
    data class EnteredDescription(val value: String): EditRuleEvent()
    data class EnteredPriority(val value: Int): EditRuleEvent()
    data class ChoosedTrigger(val value: String): EditRuleEvent()
    data class RemoveTag(val value: Int): EditRuleEvent()
}