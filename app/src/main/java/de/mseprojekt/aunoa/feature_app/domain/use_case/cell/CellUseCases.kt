package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

data class CellUseCases(
    val getCellIdsByRegion: GetCellIdsByRegion,
    val insertCell: InsertCell,
    val insertRegion: InsertRegion,
    val removeCell: RemoveCell,
    val removeRegion: RemoveRegion,
    val getRegions: GetRegions
)
