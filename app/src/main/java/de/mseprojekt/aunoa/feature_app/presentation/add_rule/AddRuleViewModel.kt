package de.mseprojekt.aunoa.feature_app.presentation.add_rule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRuleViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(AddRuleState())
    val state: State<AddRuleState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddRuleEvent) {
        when (event) {
            is AddRuleEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    title = event.value
                )
            }
            is AddRuleEvent.EnteredDescription -> {
                _state.value = _state.value.copy(
                    description = event.value
                )
            }
            is AddRuleEvent.ChoosedTrigger -> {
                _state.value = _state.value.copy(
                    triggerObjectName = event.value
                )
                println(state.value.triggerObjectName)
            }
            is AddRuleEvent.SaveRule -> {
                viewModelScope.launch {
                    try {
                        ruleUseCases.insertRule(
                            trigger = state.value.trigger,
                            action = state.value.action,
                            triggerObjectName = "TimeTrigger",
                            actionObjectName = "VolumeAction",
                            title = state.value.title,
                            description = state.value.description,
                            priority = state.value.priority,
                        )
                        _eventFlow.emit(UiEvent.SaveRule(message = "Successfully saved"))
                        println("successfully saved")
                    } catch(e: Error) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                        println(e)
                    }
                }

            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class SaveRule(val message: String): UiEvent()
    }
}