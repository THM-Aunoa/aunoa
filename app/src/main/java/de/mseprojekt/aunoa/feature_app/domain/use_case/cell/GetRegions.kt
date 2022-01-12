package de.mseprojekt.aunoa.feature_app.domain.use_case.cell

import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository

class GetRegions(
    private val repository: CellRepository
) {
    operator fun invoke(): List<Region> {
        return repository.getRegions()
    }
}
