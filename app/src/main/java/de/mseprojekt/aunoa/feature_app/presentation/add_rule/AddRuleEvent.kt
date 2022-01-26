package de.mseprojekt.aunoa.feature_app.presentation.add_rule

import de.mseprojekt.aunoa.feature_app.domain.model.Rule

sealed class AddRuleEvent {
    object SaveRule: AddRuleEvent()
    data class EnteredTitle(val value: String): AddRuleEvent()
    data class EnteredDescription(val value: String): AddRuleEvent()
    data class ChoosedTrigger(val value: String): AddRuleEvent()
}