package de.mseprojekt.aunoa.feature_app.domain.model

data class LastCellsWithRegion(
    val cellId: Long,

    val date: Long,

    val regionId: Int? = null,
    val regionName: String? = null
)
