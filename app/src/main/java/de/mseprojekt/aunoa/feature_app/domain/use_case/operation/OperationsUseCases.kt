package de.mseprojekt.aunoa.feature_app.domain.use_case.operation

data class OperationsUseCases(
    val getOperation: GetOperations,
    val getOperationsById: GetOperationsById,
    val insertOperation: InsertOperation
)
