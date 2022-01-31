package de.mseprojekt.aunoa.feature_app.presentation.operation

import android.app.Application
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.cell.CellUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.OperationsUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.user.UserUseCases
import de.mseprojekt.aunoa.other.AunoaEventInterface
import de.mseprojekt.aunoa.other.AunoaViewModelInterface
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND
import de.mseprojekt.aunoa.services.INTENT_SCAN_REGION
import de.mseprojekt.aunoa.services.INTENT_SCAN_UNTIL
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
@ExperimentalMaterialApi
class OperationViewModel @Inject constructor(
    private val operationsUseCases: OperationsUseCases,
    private val userUseCases: UserUseCases,
    private val stateUseCases: StateUseCases,
    private val cellUseCases: CellUseCases,
    private val application: Application
) : ViewModel(), AunoaViewModelInterface {

    private val _state = mutableStateOf(OperationState())
    val state: State<OperationState> = _state

    private val _eventFlow = MutableSharedFlow<OperationViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getOperationsJob: Job? = null
    private var getLastCellsJob: Job? = null

    init {
        getOperations()
        getLastCells()
        viewModelScope.launch {
            userUseCases.getUser().also { user ->
                if (user != null) {
                    _state.value = _state.value.copy(
                        username = user.name,
                        email = user.eMail
                    )
                }
            }
            stateUseCases.getCurrentState().also { appState ->
                _state.value = _state.value.copy(
                    appState = appState
                )
            }
            cellUseCases.getRegions().also { regions ->
                _state.value = _state.value.copy(
                    regions = regions
                )
            }
        }
    }

    override fun onEvent(event: AunoaEventInterface) {
        when (event) {
            is OperationEvent.EnteredUsername -> {
                _state.value = _state.value.copy(
                    username = event.value
                )
            }
            is OperationEvent.EnteredEmail -> {
                _state.value = _state.value.copy(
                    email = event.value
                )
            }
            is OperationEvent.SaveUserDetails -> {
                viewModelScope.launch {
                    userUseCases.insertUser(state.value.username, state.value.email)
                    _eventFlow.emit(OperationViewModel.UiEvent.SaveUser(message = "Successfully saved"))
                }
            }
            is OperationEvent.ToggleAppState -> {
                viewModelScope.launch {
                    stateUseCases.insertState(event.value)
                    _state.value = _state.value.copy(
                        appState = event.value
                    )
                    val intent2 = Intent(application, AunoaService::class.java)
                    if (event.value){
                        intent2.putExtra(INTENT_COMMAND, "Start")
                    } else{
                        intent2.putExtra(INTENT_COMMAND, "Exit")
                    }
                    application.startForegroundService(intent2)
                }
            }
            is OperationEvent.DeleteRegion -> {
                viewModelScope.launch {
                    cellUseCases.removeRegion(event.value)
                    cellUseCases.getRegions().also { regions ->
                        _state.value = _state.value.copy(
                            regions = regions
                        )
                    }
                }
            }
            is OperationEvent.AddRegion -> {
                viewModelScope.launch {
                    val time = LocalDateTime.now().plusMinutes(event.minutes).toEpochSecond(
                        ZoneOffset.UTC).toString()
                    cellUseCases.insertRegion(event.value, time.toLong(), event.minutes)
                    cellUseCases.getRegions().also { regions ->
                        _state.value = _state.value.copy(
                            regions = regions
                        )
                    }
                    val intent2 = Intent(application, AunoaService::class.java)
                    intent2.putExtra(INTENT_COMMAND, "Scan")
                    intent2.putExtra(INTENT_SCAN_UNTIL, time)
                    intent2.putExtra(INTENT_SCAN_REGION, "Home")
                    application.startForegroundService(intent2)
                }
            }
            is OperationEvent.EditRegion -> {
                viewModelScope.launch {
                    val time = LocalDateTime.now().plusMinutes(event.minutes).toEpochSecond(
                        ZoneOffset.UTC).toString()
                    cellUseCases.editRegion(event.id,event.value, time.toLong(), event.minutes)
                    cellUseCases.getRegions().also { regions ->
                        _state.value = _state.value.copy(
                            regions = regions
                        )
                    }
                    val intent2 = Intent(application, AunoaService::class.java)
                    intent2.putExtra(INTENT_COMMAND, "Scan")
                    intent2.putExtra(INTENT_SCAN_UNTIL, time)
                    intent2.putExtra(INTENT_SCAN_REGION, "Home")
                    application.startForegroundService(intent2)
                }
            }
            is OperationEvent.RemoveCell -> {
                viewModelScope.launch {
                    cellUseCases.removeCell(event.id)
                }

            }
        }
    }

    private fun getOperations() {
        getOperationsJob?.cancel()
        getOperationsJob = operationsUseCases.getOperationsWithRuleAndTags()
            .onEach { operations ->
                _state.value = state.value.copy(
                    operations = operations
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getLastCells() {
        getLastCellsJob?.cancel()
        getLastCellsJob = cellUseCases.getLastCells()
            .onEach { cells ->
                _state.value = state.value.copy(
                    cells = cells
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class SaveUser(val message: String) : UiEvent()
        data class ReturnRegionId(val id: Int) : UiEvent()
    }
}