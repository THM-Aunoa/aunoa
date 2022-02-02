package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.mseprojekt.aunoa.feature_app.data.util.Converters
import de.mseprojekt.aunoa.feature_app.domain.model.*


@Database(
    entities = [Rule::class, Operation::class, Act::class, Trig::class, Tag::class, RuleTagCrossRef::class, State::class, Cell::class, Region::class, User::class, LastCells::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val operationDao: OperationDao
    abstract val ruleDao: RuleDao
    abstract val stateDao: StateDao
    abstract val cellDao: CellDao
    abstract val userDao : UserDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}