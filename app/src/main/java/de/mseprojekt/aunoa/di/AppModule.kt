package de.mseprojekt.aunoa.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.mseprojekt.aunoa.feature_app.data.data_source.AppDatabase
import de.mseprojekt.aunoa.feature_app.data.repository.CellRepositoryImpl
import de.mseprojekt.aunoa.feature_app.data.repository.OperationRepositoryImpl
import de.mseprojekt.aunoa.feature_app.data.repository.RuleRepositoryImpl
import de.mseprojekt.aunoa.feature_app.data.repository.StateRepositoryImpl
import de.mseprojekt.aunoa.feature_app.domain.repository.CellRepository
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import de.mseprojekt.aunoa.feature_app.domain.repository.StateRepository
import de.mseprojekt.aunoa.feature_app.domain.use_case.cell.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.OperationsUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.GetOperations
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.GetOperationsById
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.InsertOperation
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
    fun provideCellRepository(db: AppDatabase): CellRepository {
        return CellRepositoryImpl(db.cellDao)
    }


    @Provides
    @Singleton
    fun provideRuleUseCases(repository: RuleRepository): RuleUseCases {
        return RuleUseCases(
            getRule = GetRule(repository),
            getRules = GetRules(repository),
            getRulesWithTags = GetRulesWithTags(repository),
            insertRule = InsertRule(repository),
            getRulesWithoutFlow = GetRulesWithoutFlow(repository),
            setActive = SetActive(repository),
            setEnabled = SetEnabled(repository),
            removeRule = RemoveRule(repository),
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

    @Provides
    @Singleton
    fun provideCellUseCases(repository: CellRepository) : CellUseCases {
        return CellUseCases(
            getCellIdsByRegion = GetCellIdsByRegion(repository),
            insertCell = InsertCell(repository),
            insertRegion = InsertRegion(repository),
            removeCell = RemoveCell(repository),
            removeRegion = RemoveRegion(repository),
            getRegions = GetRegions(repository),
            getRegionIdByName = GetRegionIdByName(repository)
        )
    }
}