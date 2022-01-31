package de.mseprojekt.aunoa.feature_app.presentation.operation

import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.model.Region
import kotlinx.coroutines.flow.Flow

data class OperationState (
    val operations: List<Operation> = emptyList(),
    val searchText: String = "",
    val username: String = "",
    val email: String = "",
    val appState: Boolean = false,
    val regions: List<Region> = emptyList()
)