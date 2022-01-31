package de.mseprojekt.aunoa.feature_app.domain.use_case.operation

data class OperationsUseCases(
    val getOperationsWithRuleAndTags: GetOperationsWithRuleAndTags,
    val getRulesWithOperations: GetRulesWithOperations,
    val getOperationsById: GetOperationsById,
    val insertOperation: InsertOperation,
    val deleteOldOperations: DeleteOldOperations
)
