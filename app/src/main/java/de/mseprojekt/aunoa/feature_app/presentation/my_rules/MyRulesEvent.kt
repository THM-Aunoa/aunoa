package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import de.mseprojekt.aunoa.other.AunoaEventInterface

sealed class MyRulesEvent : AunoaEventInterface {
    object EditRule: MyRulesEvent()
    object DeleteRule: MyRulesEvent()
}