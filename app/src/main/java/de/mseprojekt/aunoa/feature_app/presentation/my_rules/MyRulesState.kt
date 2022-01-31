package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags

data class MyRulesState (
    val rules: List<RuleWithTags> = emptyList(),
    val searchText: String = ""
)