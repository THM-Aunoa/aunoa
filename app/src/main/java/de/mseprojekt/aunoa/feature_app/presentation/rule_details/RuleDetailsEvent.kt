package de.mseprojekt.aunoa.feature_app.presentation.rule_details

import de.mseprojekt.aunoa.other.AunoaEventInterface

sealed class RuleDetailsEvent : AunoaEventInterface {
    object DeleteRule : RuleDetailsEvent()
}