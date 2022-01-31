package de.mseprojekt.aunoa.feature_app.presentation.rule_details

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Tag

data class RuleDetailsState (
    val rule: RuleWithActAndTrig? = null,
    val tags: List<Tag> = emptyList(),
    val operations: List<Operation> = emptyList(),
)