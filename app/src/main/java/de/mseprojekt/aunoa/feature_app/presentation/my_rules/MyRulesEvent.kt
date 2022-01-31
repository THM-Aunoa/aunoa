package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubEvent
import de.mseprojekt.aunoa.other.AunoaEventInterface

sealed class MyRulesEvent : AunoaEventInterface {
    data class SearchRules(val searchText: String): MyRulesEvent()
    data class FilterRules(val filterTag: Tag): MyRulesEvent()
    data class DeleteRule(val value: Int): MyRulesEvent()
    object ResetFilter: MyRulesEvent()
}