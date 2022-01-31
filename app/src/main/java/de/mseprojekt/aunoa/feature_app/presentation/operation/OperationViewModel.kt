package de.mseprojekt.aunoa.feature_app.presentation.operation

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val operationsUseCases: OperationsUseCases,
    private val userUseCases: UserUseCases,
    private val stateUseCases: StateUseCases,
    private val cellUseCases: CellUseCases,
) : ViewModel(), AunoaViewModelInterface {

    private val _state = mutableStateOf(OperationState())
    val state: State<OperationState> = _state

    private val _eventFlow = MutableSharedFlow<OperationViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getOperationsJob: Job? = null

    init {
        getOperations()
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
                println(regions);
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

    sealed class UiEvent {
        data class SaveUser(val message: String) : UiEvent()
    }
}