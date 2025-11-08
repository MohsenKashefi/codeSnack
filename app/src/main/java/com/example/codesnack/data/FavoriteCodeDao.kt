package com.example.codesnack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteCodeDao {
    @Query("SELECT * FROM favorite_codes ORDER BY createdAt DESC")
    suspend fun getAllFavorites(): List<FavoriteCode>

    @Query("SELECT * FROM favorite_codes WHERE language = :language ORDER BY createdAt DESC")
    suspend fun getFavoritesByLanguage(language: String): List<FavoriteCode>

    @Insert
    suspend fun insertFavorite(favoriteCode: FavoriteCode): Long

    @Delete
    suspend fun deleteFavorite(favoriteCode: FavoriteCode)

    @Query("DELETE FROM favorite_codes WHERE id = :id")
    suspend fun deleteFavoriteById(id: Long)
}