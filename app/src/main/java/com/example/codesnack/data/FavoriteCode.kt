package com.example.codesnack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_codes")
data class FavoriteCode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val language: String,
    val code: String,
    val title: String = "",
    val createdAt: Long = System.currentTimeMillis()
)