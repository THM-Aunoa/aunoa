package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubEvent
import de.mseprojekt.aunoa.other.AunoaEventInterface
import de.mseprojekt.aunoa.other.AunoaViewModelInterface
import javax.inject.Inject

@HiltViewModel
class MyRulesViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
) : ViewModel(), AunoaViewModelInterface {

    private val initRules: List<RuleWithTags> = ruleUseCases.getRulesWithTagsWithoutFlow()
    private val _state = mutableStateOf(MyRulesState())
    val state: State<MyRulesState> = _state

    init {
        _state.value = state.value.copy(rules = initRules)
    }

    override fun onEvent(event: AunoaEventInterface) {
        when (event) {
            is RulesHubEvent.SearchRules -> {
                val rules = initRules.filter { it.rule.title.lowercase().contains(event.searchText.lowercase()) }
                _state.value = state.value.copy(rules = rules, searchText = event.searchText)
            }
        }
    }
}
