package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.UserDao
import de.mseprojekt.aunoa.feature_app.domain.model.User
import de.mseprojekt.aunoa.feature_app.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
): UserRepository {
    override fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}