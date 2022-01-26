package de.mseprojekt.aunoa.feature_app.domain.use_case.operation

data class OperationsUseCases(
    val getOperations: GetOperations,
    val getOperationsWithRule: GetOperationsWithRule,
    val getOperationsById: GetOperationsById,
    val insertOperation: InsertOperation
)
