package de.mseprojekt.aunoa.feature_app.data.repository

import com.google.gson.Gson
import de.mseprojekt.aunoa.feature_app.data.data_source.CellDao
import de.mseprojekt.aunoa.feature_app.data.data_source.RuleDao
import de.mseprojekt.aunoa.feature_app.domain.model.Cell
import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.CellTrigger
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class CellRepositoryImpl (
    private val cellDao: CellDao,
    private val ruleDao : RuleDao
): CellRepository {
    override fun getCellIdsByRegion(name: String): List<Long> {
        val callable = Callable{ cellDao.getCellIdsByRegion(name) }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override fun getRegionIdByName(name: String): Int? {
        val callable = Callable{ cellDao.getRegionIdByName(name) }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override suspend fun insertRegion(name: String) {
        if (getRegionIdByName(name) == null) {
            cellDao.insertRegion(Region(name = name))
        }
    }

    override fun getRegionNameById(id: Int): String?{
        val callable = Callable{ cellDao.getRegionNameById(id) }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override fun deleteRegion(id: Int) {
        val callable = Callable{ ruleDao.getRulesWithoutFlow() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        val rules = future!!.get()
        val gson = Gson()
        for (rule in rules){
            if (rule.content.trig.triggerType == "CellTrigger"){
                val cellTrig: CellTrigger = gson.fromJson(
                    rule.content.trig.triggerObject,
                    CellTrigger::class.java) as CellTrigger
                if (cellTrig.name == getRegionNameById(id)){
                    rule.rule.ruleId?.let {
                        val callable3 = Callable{ ruleDao.deleteRule(it)  }

                        val future3 = Executors.newSingleThreadExecutor().submit(callable3)

                        future3!!.get()
                    }
                }
            }
        }
        val callable2 = Callable{ cellDao.deleteRegion(id) }

        val future2 = Executors.newSingleThreadExecutor().submit(callable2)

        future2!!.get()
    }

    override fun insertCell(regionId: Int, cellId: Long) {
        val callable = Callable{ cellDao.insertCell(Cell(regionId = regionId, cellId = cellId)) }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override suspend fun deleteCell(cellId: Long) {
        cellDao.deleteCell(cellId)
    }

    override fun getRegions(): List<Region> {
        val callable = Callable{ cellDao.getRegions() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }
}