package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.Tag

data class MyRulesState(
    val rules: List<RuleWithTags> = emptyList(),
    val searchText: String = "",
    val filterTag: Tag? = null
)