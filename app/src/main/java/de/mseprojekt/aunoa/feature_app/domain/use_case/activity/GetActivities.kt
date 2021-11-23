package de.mseprojekt.aunoa.feature_app.domain.use_case.activity

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActivities
import de.mseprojekt.aunoa.feature_app.domain.model.Activity
import de.mseprojekt.aunoa.feature_app.domain.repository.ActivityRepository

class GetActivities (
    private val repository: ActivityRepository
    ){
    suspend operator fun invoke(id: Int): List<Activity> {
        return repository.getActivitiesById(id)

    }
}