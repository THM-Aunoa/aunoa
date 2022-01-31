package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.*
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.OperationWithRuleAndTags
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithOperations
import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {

    @Transaction
    @Query("SELECT * FROM operation")
    fun getOperationsWithRuleAndTags(): Flow<List<OperationWithRuleAndTags>>

    @Query("SELECT * FROM rule")
    fun getRulesWithOperations(): Flow<List<RuleWithOperations>>

    @Query("SELECT * FROM operation WHERE ruleId = :id")
    suspend fun getOperationsById(id: Int): List<Operation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation : Operation)
}