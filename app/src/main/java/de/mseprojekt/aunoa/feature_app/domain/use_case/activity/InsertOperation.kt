package de.mseprojekt.aunoa.feature_app.domain.use_case.activity


import de.mseprojekt.aunoa.feature_app.domain.model.Operation
import de.mseprojekt.aunoa.feature_app.domain.model.StatusType
import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class InsertOperation(
    private val repository: OperationRepository
) {
    suspend operator fun invoke(ruleId: Int, status: Boolean) {
        val statusType = if (status) StatusType.Success else StatusType.Failure
        return repository.insertOperation(
            Operation(
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                ruleId = ruleId,
                status = statusType
            )
        )
    }
}
