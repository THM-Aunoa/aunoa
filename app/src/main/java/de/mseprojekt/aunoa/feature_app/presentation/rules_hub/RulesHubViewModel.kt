package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub.RulesHubUseCases
import de.mseprojekt.aunoa.feature_app.presentation.add_rule.AddRuleEvent
import javax.inject.Inject


@HiltViewModel
class RulesHubViewModel @Inject constructor(
    rulesHubUseCases: RulesHubUseCases
) : ViewModel(){

    private val initRules: List<UnzippedRuleWithTags> = rulesHubUseCases.getHubRules()
    private val _state = mutableStateOf(RulesHubState())
    val state: State<RulesHubState> = _state

    init {
        _state.value = state.value.copy(rules = initRules)
    }

    fun onEvent(event: RulesHubEvent) {
        when (event) {
            is RulesHubEvent.SearchRules -> {
                val rules = initRules.filter { it.rule.title.lowercase().contains(event.searchText.lowercase()) }
                _state.value = state.value.copy(rules = rules, searchText = event.searchText)
            }
        }
    }
}