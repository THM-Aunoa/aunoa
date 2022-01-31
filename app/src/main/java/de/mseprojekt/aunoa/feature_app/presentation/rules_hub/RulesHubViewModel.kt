package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub.RulesHubUseCases
import de.mseprojekt.aunoa.other.AunoaEventInterface
import de.mseprojekt.aunoa.other.AunoaViewModelInterface
import javax.inject.Inject


@HiltViewModel
class RulesHubViewModel @Inject constructor(
    rulesHubUseCases: RulesHubUseCases
) : ViewModel(), AunoaViewModelInterface {

    private val initRules: List<UnzippedRuleWithTags> = rulesHubUseCases.getHubRules()
    private val _state = mutableStateOf(RulesHubState())
    val state: State<RulesHubState> = _state

    init {
        _state.value = state.value.copy(rules = initRules)
    }

    override fun onEvent(event: AunoaEventInterface) {
        when (event) {
            is RulesHubEvent.SearchRules -> {
                val rules = initRules.filter { it.rule.title.lowercase().contains(event.searchText.lowercase()) }
                _state.value = state.value.copy(rules = rules, searchText = event.searchText)
            }
            is RulesHubEvent.FilterRules -> {
                val rules = initRules.filter { it.tags.contains(event.filterTag) }
                _state.value = state.value.copy(rules = rules, filterTag = event.filterTag)
            }
            is RulesHubEvent.ResetFilter -> {
                _state.value = state.value.copy(rules = initRules)
            }
        }
    }

}