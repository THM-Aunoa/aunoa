package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.CellDao
import de.mseprojekt.aunoa.feature_app.domain.model.Cell
import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class CellRepositoryImpl (
    private val dao: CellDao
): CellRepository {
    override fun getCellIdsByRegion(name: String): List<Long> {
        val callable = Callable{ dao.getCellIdsByRegion(name) }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override suspend fun insertRegion(name: String) {
        dao.insertRegion(Region(name = name))
    }

    override suspend fun deleteRegion(id: Int) {
        dao.deleteRegion(id)
    }

    override suspend fun insertCell(regionId: Int, cellId: Long) {
        dao.insertCell(Cell(regionId = regionId, cellId = cellId))
    }

    override suspend fun deleteCell(cellId: Long) {
        dao.deleteCell(cellId)
    }

    override fun getRegions(): List<Region> {
        val callable = Callable{ dao.getRegions() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }
}