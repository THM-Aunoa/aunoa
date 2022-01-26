package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags

data class RulesHubState (
    val rules: List<RuleWithTags> = emptyList(),
    val searchText: String = ""
)