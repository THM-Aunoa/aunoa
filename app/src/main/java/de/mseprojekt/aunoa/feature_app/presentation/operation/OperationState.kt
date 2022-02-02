package de.mseprojekt.aunoa.feature_app.presentation.operation

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.OperationWithRuleAndTags
import de.mseprojekt.aunoa.feature_app.domain.model.LastCellsWithRegion
import de.mseprojekt.aunoa.feature_app.domain.model.Region

data class OperationState (
    val operations: List<OperationWithRuleAndTags> = emptyList(),
    val searchText: String = "",
    val username: String = "",
    val email: String = "",
    val appState: Boolean = false,
    val regions: List<Region> = emptyList(),
    val cells: List<LastCellsWithRegion> = emptyList()
)