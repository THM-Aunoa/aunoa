package de.mseprojekt.aunoa.feature_app.presentation.operation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.OperationsUseCases
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val operationsUseCases: OperationsUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(OperationState())
    val state: State<OperationState> = _state

    private var getOperationsJob: Job? = null

    init {
        getOperations()
    }

    fun onEvent(event: OperationEvent) {
        when (event) {

        }
    }

    private fun getOperations() {
        getOperationsJob?.cancel()
        getOperationsJob = operationsUseCases.getOperationsWithRule()
            .onEach { operations ->
                _state.value = state.value.copy(
                    operations = operations
                )
            }
            .launchIn(viewModelScope)
    }
}