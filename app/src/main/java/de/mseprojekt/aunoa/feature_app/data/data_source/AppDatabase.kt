package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.mseprojekt.aunoa.feature_app.data.util.Converters
import de.mseprojekt.aunoa.feature_app.domain.model.*
import de.mseprojekt.aunoa.feature_app.domain.model.User


@Database(
    entities = [Rule::class, Operation::class, Act::class, Trig::class, Tag::class, RuleTagCrossRef::class, State::class, Cell::class, Region::class, User::class],
    version = 1,
    exportSchema = false
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