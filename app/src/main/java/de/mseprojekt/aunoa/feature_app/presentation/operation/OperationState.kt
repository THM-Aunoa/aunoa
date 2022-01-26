package de.mseprojekt.aunoa.feature_app.presentation.operation

import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import kotlinx.coroutines.flow.Flow

data class OperationState (
    val operations: List<Operation> = emptyList(),
    val searchText: String = ""
)