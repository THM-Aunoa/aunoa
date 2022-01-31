package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags
import de.mseprojekt.aunoa.other.AunoaEventInterface

sealed class RulesHubEvent : AunoaEventInterface {
    data class SearchRules(val searchText: String): RulesHubEvent()
    data class FilterRules(val filterTag: Tag): RulesHubEvent()
    data class AddRule(val value: UnzippedRuleWithTags): RulesHubEvent()
    object ResetFilter: RulesHubEvent()
}