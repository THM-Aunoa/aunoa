package de.mseprojekt.aunoa.feature_app.domain.repository

import de.mseprojekt.aunoa.feature_app.domain.model.User

interface UserRepository {

    fun getUserById(userId: Int): User?
    suspend fun insertUser(user: User)
}