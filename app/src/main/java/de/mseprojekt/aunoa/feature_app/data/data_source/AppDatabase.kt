package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.mseprojekt.aunoa.feature_app.data.util.Converters
import de.mseprojekt.aunoa.feature_app.domain.model.Activity
import de.mseprojekt.aunoa.feature_app.domain.model.Rule


@Database(
    entities = [Rule::class, Activity::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val activityDao: ActivityDao
    abstract val ruleDao: RuleDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}