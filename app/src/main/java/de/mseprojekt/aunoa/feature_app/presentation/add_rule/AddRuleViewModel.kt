package de.mseprojekt.aunoa.feature_app.presentation.add_rule

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import de.mseprojekt.aunoa.other.foregroundStartService
import android.location.Location
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubState
import de.mseprojekt.aunoa.other.foregroundStartService
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRuleViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
    private val application: Application
) : ViewModel() {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }


    private var cancellationTokenSource = CancellationTokenSource()

    private val _state = mutableStateOf(AddRuleState())
    val state: State<AddRuleState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    @ExperimentalMaterialApi
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

            is AddRuleEvent.FillCurrentLocationBoxes ->{
                requestCurrentLocation().addOnCompleteListener { task: Task<Location> ->
                    if (task.isSuccessful && task.result != null) {
                        /* Todo add the current location into the boxes */
                    }
                }
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
                        val intent = Intent(application, AunoaService::class.java)
                        intent.putExtra(INTENT_COMMAND, "Update")
                        application.startForegroundService(intent)
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

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation(): Task<Location> {
        return fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

    }


}