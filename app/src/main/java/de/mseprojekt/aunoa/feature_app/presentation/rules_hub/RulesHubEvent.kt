package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

sealed class RulesHubEvent {
    data class SearchRules(val searchText: String): RulesHubEvent()
}