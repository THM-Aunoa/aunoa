package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActivities
import de.mseprojekt.aunoa.feature_app.domain.model.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activity")
    fun getActivities(): Flow<List<Activity>>

    @Transaction
    @Query("SELECT * FROM activity WHERE RuleId = :id")
    suspend fun getActivitiesById(id: Int): List<Activity>
}