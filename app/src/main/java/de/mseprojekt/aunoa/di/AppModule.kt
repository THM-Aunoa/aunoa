package de.mseprojekt.aunoa.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.mseprojekt.aunoa.feature_app.data.data_source.AppDatabase
import de.mseprojekt.aunoa.feature_app.data.remote.RulesHubAPI
import de.mseprojekt.aunoa.feature_app.data.repository.*
import de.mseprojekt.aunoa.feature_app.domain.repository.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.cell.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.operation.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.*
import de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub.GetHubRules
import de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub.RulesHubUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.GetCurrentState
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.InsertState
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.IsFirstRun
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.user.GetUser
import de.mseprojekt.aunoa.feature_app.domain.use_case.user.InsertUser
import de.mseprojekt.aunoa.feature_app.domain.use_case.user.UserUseCases
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
    fun provideUserRepository(db: AppDatabase): UserRepository {
        return UserRepositoryImpl(db.userDao)
    }

    @Provides
    @Singleton
    fun provideRulesHubAPI(): RulesHubAPI {
        return RulesHubAPI()
    }


    @Provides
    @Singleton
    fun provideRuleUseCases(repository: RuleRepository): RuleUseCases {
        return RuleUseCases(
            getRule = GetRule(repository),
            getRules = GetRules(repository),
            getRulesWithTags = GetRulesWithTags(repository),
            getRuleWithTags = GetRuleWithTags(repository),
            getRulesWithTagsWithoutFlow = GetRulesWithTagsWithoutFlow(repository),
            insertRule = InsertRule(repository),
            getRulesWithoutFlow = GetRulesWithoutFlow(repository),
            setActive = SetActive(repository),
            setEnabled = SetEnabled(repository),
            removeRule = RemoveRule(repository),
            getTags = GetTags(repository),
//            getTagsForRule = GetTagsForRule(repository),
            insertRuleTagCrossRef = InsertRuleTagCrossRef(repository),
            insertTag = InsertTag(repository),
            insertTags = InsertTags(repository),
            clearTagsForRule = ClearTagsForRule(repository)
        )
    }
    @Provides
    @Singleton
    fun provideOperationUseCases(repository: OperationRepository): OperationsUseCases {
        return OperationsUseCases(
            getOperationsWithRuleAndTags = GetOperationsWithRuleAndTags(repository),
            getRulesWithOperations = GetRulesWithOperations(repository),
            getOperationsById = GetOperationsById(repository),
            insertOperation = InsertOperation(repository),
            deleteOldOperations = DeleteOldOperations(repository)
        )
    }

    @Provides
    @Singleton
    fun provideStateUseCases(repository: StateRepository) : StateUseCases{
        return StateUseCases(
            getCurrentState = GetCurrentState(repository),
            insertState = InsertState(repository),
            isFirstRun = IsFirstRun(repository)
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
    @Provides
    @Singleton
    fun provideUserUseCases(repository: UserRepository) : UserUseCases {
        return UserUseCases(
            getUser = GetUser(repository),
            insertUser = InsertUser(repository)
        )
    }

    @Provides
    @Singleton
    fun provideRulesHubUseCases(api: RulesHubAPI) : RulesHubUseCases {
        return RulesHubUseCases(
            getHubRules = GetHubRules(api)
        )
    }

}