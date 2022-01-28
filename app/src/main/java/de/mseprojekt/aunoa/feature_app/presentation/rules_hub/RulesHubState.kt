package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags

data class RulesHubState (
    val rules: List<UnzippedRuleWithTags> = emptyList(),
    val searchText: String = ""
)