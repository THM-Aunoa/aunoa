package de.mseprojekt.aunoa.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.mseprojekt.aunoa.feature_app.data.data_source.AppDatabase
import de.mseprojekt.aunoa.feature_app.data.repository.OperationsRepositoryImpl
import de.mseprojekt.aunoa.feature_app.data.repository.RuleRepositoryImpl
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationsRepository
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.OperationsUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.GetOperations
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.GetRule
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.GetRules
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.InsertRule
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRuleRepository(db: AppDatabase): RuleRepository {
        return RuleRepositoryImpl(db.ruleDao)
    }
    @Provides
    @Singleton
    fun provideActivityRepository(db: AppDatabase): OperationsRepository {
        return OperationsRepositoryImpl(db.activityDao)
    }

    @Provides
    @Singleton
    fun provideRuleUseCases(repository: RuleRepository): RuleUseCases {
        return RuleUseCases(
            getRule = GetRule(repository),
            getRules = GetRules(repository),
            insertRule = InsertRule(repository)
        )
    }
    @Provides
    @Singleton
    fun provideActivityUseCases(repository: OperationsRepository): OperationsUseCases {
        return OperationsUseCases(
            getOperation = GetOperations(repository),
        )
    }
}