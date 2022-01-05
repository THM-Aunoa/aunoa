package de.mseprojekt.aunoa.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.mseprojekt.aunoa.feature_app.data.data_source.AppDatabase
import de.mseprojekt.aunoa.feature_app.data.repository.OperationRepositoryImpl
import de.mseprojekt.aunoa.feature_app.data.repository.RuleRepositoryImpl
import de.mseprojekt.aunoa.feature_app.data.repository.StateRepositoryImpl
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.OperationsUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.GetOperations
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.GetOperationsById
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.InsertOperation
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.GetCurrentState
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.InsertState
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
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
    fun provideOperationRepository(db: AppDatabase): OperationRepository {
        return OperationRepositoryImpl(db.operationDao)
    }
    @Provides
    @Singleton
    fun provideStateRepository(db: AppDatabase): StateRepository{
        return StateRepositoryImpl(db.stateDao)
    }


    @Provides
    @Singleton
    fun provideRuleUseCases(repository: RuleRepository): RuleUseCases {
        return RuleUseCases(
            getRule = GetRule(repository),
            getRules = GetRules(repository),
            getRulesWithTags = GetRulesWithTags(repository),
            insertRule = InsertRule(repository)
        )
    }
    @Provides
    @Singleton
    fun provideOperationUseCases(repository: OperationRepository): OperationsUseCases {
        return OperationsUseCases(
            getOperation = GetOperations(repository),
            getOperationsById = GetOperationsById(repository),
            insertOperation = InsertOperation(repository)
        )
    }

    @Provides
    @Singleton
    fun provideStateUseCases(repository: StateRepository) : StateUseCases{
        return StateUseCases(
            getCurrentState = GetCurrentState(repository),
            insertState = InsertState(repository)
        )
    }
}