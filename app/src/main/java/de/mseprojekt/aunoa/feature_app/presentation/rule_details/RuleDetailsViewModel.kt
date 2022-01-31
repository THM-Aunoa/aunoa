package de.mseprojekt.aunoa.feature_app.presentation.rule_details

import android.app.Application
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.edit_rule.EditRuleEvent
import de.mseprojekt.aunoa.feature_app.presentation.edit_rule.EditRuleViewModel
import de.mseprojekt.aunoa.other.AunoaEventInterface
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RuleDetailsViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(RuleDetailsState())
    val state: State<RuleDetailsState> = _state

    private val _eventFlow = MutableSharedFlow<RuleDetailsViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("ruleId")?.let { ruleId ->
            viewModelScope.launch {
                val rule = ruleUseCases.getRule(ruleId)
                val tags = ruleUseCases.getRuleWithTags(ruleId)
                _state.value = state.value.copy(rule = rule, tags = tags.tags)
            }
        }
    }

    @ExperimentalMaterialApi
    fun onEvent(event: AunoaEventInterface) {
        when (event) {
            is RuleDetailsEvent.DeleteRule -> {
                if (state.value.rule != null) {
                    viewModelScope.launch {
                        try {
                            ruleUseCases.removeRule(state.value.rule!!.rule.ruleId!!)
                            _eventFlow.emit(RuleDetailsViewModel.UiEvent.DeleteRule(message = "Successfully deleted"))
                            println("successfully deleted")
                            val intent = Intent(application, AunoaService::class.java)
                            intent.putExtra(INTENT_COMMAND, "Update")
                            application.startForegroundService(intent)
                        } catch (e: Error) {
                            _eventFlow.emit(
                                RuleDetailsViewModel.UiEvent.ShowSnackbar(
                                    message = e.message ?: "Couldn't delete note"
                                )
                            )
                            println(e)
                        }
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


