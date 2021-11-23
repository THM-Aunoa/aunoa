package de.mseprojekt.aunoa.feature_app.data.repository


import de.mseprojekt.aunoa.feature_app.data.data_source.RuleDao
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository

class RuleRepositoryImpl(
    private val dao: RuleDao
): RuleRepository {
    override suspend fun getRuleById(id: Int): Rule? {
        return dao.getRuleById(id);
    }
}