package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.location.Location
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.LocationTrigger
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRuleViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }


    private var cancellationTokenSource = CancellationTokenSource()

    private val _state = mutableStateOf(EditRuleState())
    val state: State<EditRuleState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("ruleId")?.let { ruleId ->
            if (ruleId != -1) {
                viewModelScope.launch {
                    ruleUseCases.getRuleWithTags(ruleId).also { rule ->
                        val tags = ruleUseCases.getRuleWithTags(ruleId);
                        _state.value = _state.value.copy(
                            ruleId = ruleId,
                            title = rule.rule.title,
                            description = rule.rule.description,
                            tags = tags.tags,
                            priority = rule.rule.priority
                        )
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    fun onEvent(event: EditRuleEvent) {
        when (event) {
            is EditRuleEvent.DeleteRule -> {
                if (state.value.ruleId != -1) {
                    viewModelScope.launch {
                        try {
                            ruleUseCases.removeRule(state.value.ruleId)
                            _eventFlow.emit(UiEvent.DeleteRule(message = "Successfully deleted"))
                            println("successfully deleted")
                            val intent = Intent(application, AunoaService::class.java)
                            intent.putExtra(INTENT_COMMAND, "Update")
                            application.startForegroundService(intent)
                        } catch (e: Error) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = e.message ?: "Couldn't delete note"
                                )
                            )
                            println(e)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.DeleteRule(message = "Nothing to delete"))
                    }
                }
            }
            is EditRuleEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    title = event.value
                )
            }
            is EditRuleEvent.EnteredDescription -> {
                _state.value = _state.value.copy(
                    description = event.value
                )
            }
            is EditRuleEvent.EnteredPriority -> {
                _state.value = _state.value.copy(
                    priority = event.value
                )
            }
            is EditRuleEvent.AddTag -> {
                val newTags = state.value.tags + Tag(title = event.value)
                _state.value = _state.value.copy(
                    tags = newTags
                )
            }
            is EditRuleEvent.ChoosedTrigger -> {
                _state.value = _state.value.copy(
                    triggerObjectName = event.value
                )
            }

            is EditRuleEvent.FillCurrentLocationBoxes -> {
                requestCurrentLocation().addOnCompleteListener { task: Task<Location> ->
                    if (task.isSuccessful && task.result != null) {
                        _state.value = _state.value.copy(
                            trigger = LocationTrigger(
                                latitude = task.result.latitude,
                                longitude = task.result.longitude,
                                radius = 10.0
                            )
                        )
                    }
                }
            }
            is EditRuleEvent.SaveRule -> {
                viewModelScope.launch {
                    try {
                        val ruleId = ruleUseCases.insertRule(
                            trigger = state.value.trigger,
                            action = state.value.action,
                            title = state.value.title,
                            description = state.value.description,
                            priority = state.value.priority,
                            id = state.value.ruleId
                        )
                        ruleUseCases.clearTagsForRule(ruleId)
                        val newTags = ruleUseCases.insertTags(state.value.tags)
                        newTags.forEach { tag ->
                            ruleUseCases.insertRuleTagCrossRef(ruleId, tag.tagId!!)
                        }
                        _eventFlow.emit(UiEvent.SaveRule(message = "Successfully saved"))
                        println("successfully saved")
                        val intent = Intent(application, AunoaService::class.java)
                        intent.putExtra(INTENT_COMMAND, "Update")
                        application.startForegroundService(intent)

                    } catch (e: Error) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                        println(e)
                    }
                }
            }
            is EditRuleEvent.RemoveTag -> {
                val newTags = state.value.tags.filter { it.tagId != event.value }
                _state.value = _state.value.copy(
                    tags = newTags
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class SaveRule(val message: String) : UiEvent()
        data class DeleteRule(val message: String) : UiEvent()
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation(): Task<Location> {
        return fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

    }


}