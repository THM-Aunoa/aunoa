package de.mseprojekt.aunoa.feature_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val userId: Int? = null,
    val name: String,
    val eMail: String
)
