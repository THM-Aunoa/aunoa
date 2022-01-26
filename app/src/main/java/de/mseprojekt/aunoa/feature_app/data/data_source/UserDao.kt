package de.mseprojekt.aunoa.feature_app.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.mseprojekt.aunoa.feature_app.domain.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : User)

    @Query("SELECT * FROM user where userId= :userId")
    fun getUserById(userId: Int): User?
}