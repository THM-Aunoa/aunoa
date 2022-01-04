package de.mseprojekt.aunoa.feature_app.domain.use_case.activity


data class OperationsUseCases(
    val getOperation: GetOperations,
    val getOperationsById: GetOperationsById,
    val insertOperation: InsertOperation
)
