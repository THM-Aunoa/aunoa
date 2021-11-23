package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.ActivityDao
import de.mseprojekt.aunoa.feature_app.data.data_source.RuleDao
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActivities
import de.mseprojekt.aunoa.feature_app.domain.model.Activity
import de.mseprojekt.aunoa.feature_app.domain.repository.ActivityRepository

class ActivityRepositoryImpl(
    private val dao: ActivityDao
): ActivityRepository {
    override suspend fun getActivitiesById(id: Int): List<Activity> {
        return dao.getActivitiesById(id)
    }

}