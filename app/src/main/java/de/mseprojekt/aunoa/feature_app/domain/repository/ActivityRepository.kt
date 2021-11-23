package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActivities
import de.mseprojekt.aunoa.feature_app.domain.model.Activity

interface ActivityRepository {

    suspend fun getActivitiesById(id: Int): List<Activity>
}