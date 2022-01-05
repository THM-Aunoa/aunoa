package de.mseprojekt.aunoa.feature_app.data.repository


import de.mseprojekt.aunoa.feature_app.data.data_source.RuleDao
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow

class RuleRepositoryImpl(
    private val dao: RuleDao
): RuleRepository {
    override suspend fun getMaxIdFromRules(): Int? {
        return dao.getMaxIdFromRules()
    }

    override suspend fun getRuleById(id: Int): RuleWithActAndTrig? {
        return dao.getRuleById(id);
    }
    override fun getRules(): Flow<List<RuleWithActAndTrig>> {
        return dao.getRules();
    }

    override fun getRulesWithTags(): Flow<List<RuleWithTags>> {
        return dao.getRulesWithTags();
    }

    override suspend fun getRulesWithoutFlow(): List<RuleWithActAndTrig> {
        return dao.getRulesWithoutFlow();
    }

    override suspend fun insertRule(rule: Rule) {
        return dao.insertRule(rule)
    }

    override suspend fun insertTrigger(trigger: Trig) {
        return dao.insertTrigger(trigger)
    }

    override suspend fun insertAction(action: Act) {
        return dao.insertAction(action)
    }
}