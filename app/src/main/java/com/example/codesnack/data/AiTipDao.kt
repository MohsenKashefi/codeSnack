package com.example.codesnack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AiTipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tip: AiTip): Long

    @Update
    suspend fun update(tip: AiTip)

    @Query("SELECT * FROM ai_tips WHERE language = :language ORDER BY usedCount ASC, generatedAt DESC LIMIT 1")
    suspend fun getLeastUsedTipByLanguage(language: String): AiTip?

    @Query("SELECT * FROM ai_tips ORDER BY usedCount ASC, generatedAt DESC LIMIT 1")
    suspend fun getLeastUsedTip(): AiTip?

    @Query("SELECT COUNT(*) FROM ai_tips WHERE language = :language")
    suspend fun getTipCountByLanguage(language: String): Int

    @Query("SELECT COUNT(*) FROM ai_tips")
    suspend fun getTotalTipCount(): Int

    @Query("DELETE FROM ai_tips WHERE id IN (SELECT id FROM ai_tips ORDER BY generatedAt ASC LIMIT :count)")
    suspend fun deleteOldestTips(count: Int)

    @Query("SELECT * FROM ai_tips ORDER BY generatedAt DESC")
    suspend fun getAllTips(): List<AiTip>
}
