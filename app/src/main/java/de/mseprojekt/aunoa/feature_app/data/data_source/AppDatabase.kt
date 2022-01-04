package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.mseprojekt.aunoa.feature_app.data.util.Converters
import de.mseprojekt.aunoa.feature_app.domain.model.Act
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.model.Rule
import de.mseprojekt.aunoa.feature_app.domain.model.Trig


@Database(
    entities = [Rule::class, Operation::class, Act::class, Trig::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val activityDao: OperationDao
    abstract val ruleDao: RuleDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}