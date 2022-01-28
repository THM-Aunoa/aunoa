package de.mseprojekt.aunoa.feature_app.domain.use_case.user

import de.mseprojekt.aunoa.feature_app.domain.model.User
import de.mseprojekt.aunoa.feature_app.domain.repository.UserRepository

class InsertUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String, eMail: String) {
        repository.insertUser(
            User(
                userId = 1,
                name = name,
                eMail = eMail
            )
        )
    }
}