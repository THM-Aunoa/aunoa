package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import android.app.Application
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.rule_details.RuleDetailsEvent
import de.mseprojekt.aunoa.feature_app.presentation.rule_details.RuleDetailsViewModel
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubEvent
import de.mseprojekt.aunoa.other.AunoaEventInterface
import de.mseprojekt.aunoa.other.AunoaViewModelInterface
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class MyRulesViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
    private val application: Application,
) : ViewModel(), AunoaViewModelInterface {

    private val initRules: List<RuleWithTags> = ruleUseCases.getRulesWithTagsWithoutFlow()
    private val _state = mutableStateOf(MyRulesState())
    val state: State<MyRulesState> = _state

    private val _eventFlow = MutableSharedFlow<MyRulesViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        _state.value = state.value.copy(rules = initRules)
    }

    override fun onEvent(event: AunoaEventInterface) {
        when (event) {
            is MyRulesEvent.SearchRules -> {
                val rules = initRules.filter {
                    it.rule.title.lowercase().contains(event.searchText.lowercase())
                }
                _state.value = state.value.copy(rules = rules, searchText = event.searchText)
            }
            is MyRulesEvent.FilterRules -> {
                val rules = initRules.filter { it.tags.contains(event.filterTag) }
                _state.value = state.value.copy(rules = rules, filterTag = event.filterTag)
            }
            is MyRulesEvent.ResetFilter -> {
                _state.value = state.value.copy(rules = initRules)
            }
            is MyRulesEvent.DeleteRule -> {
                viewModelScope.launch {
                    try {
                        ruleUseCases.removeRule(event.value)
                        _eventFlow.emit(MyRulesViewModel.UiEvent.DeleteRule(message = "Successfully deleted"))
                        val intent = Intent(application, AunoaService::class.java)
                        intent.putExtra(INTENT_COMMAND, "Update")
                        application.startForegroundService(intent)
                        _state.value = state.value.copy(rules = ruleUseCases.getRulesWithTagsWithoutFlow())
                    } catch (e: Error) {
                        _eventFlow.emit(
                            MyRulesViewModel.UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't delete note"
                            )
                        )
                        println(e)
                    }
                }

            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class DeleteRule(val message: String) : UiEvent()
    }
}
