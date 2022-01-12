package de.mseprojekt.aunoa.feature_app.presentation.operation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.OperationsUseCases
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val operationsUseCases: OperationsUseCases,
) : ViewModel()  {

    fun onEvent(event: OperationEvent) {
        when (event) {

        }
    }
}