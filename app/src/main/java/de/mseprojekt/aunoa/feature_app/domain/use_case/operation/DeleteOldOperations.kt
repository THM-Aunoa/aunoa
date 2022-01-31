package de.mseprojekt.aunoa.feature_app.domain.use_case.operation

import de.mseprojekt.aunoa.feature_app.domain.repository.OperationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class DeleteOldOperations(
    private val repository: OperationRepository
) {
    suspend operator fun invoke() {
        val date = LocalDateTime.now().minusWeeks(1).toEpochSecond(ZoneOffset.UTC)
        return repository.deleteOperations(date)
    }
}
