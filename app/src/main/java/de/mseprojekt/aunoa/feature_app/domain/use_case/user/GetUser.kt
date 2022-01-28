package de.mseprojekt.aunoa.feature_app.domain.use_case.user

import de.mseprojekt.aunoa.feature_app.domain.model.User
import de.mseprojekt.aunoa.feature_app.domain.repository.UserRepository

class GetUser(
    private val repository: UserRepository
) {
    operator fun invoke(): User? {
        return repository.getUserById(1)
    }
}