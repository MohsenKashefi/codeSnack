package com.example.codesnack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_tips")
data class AiTip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val language: String,
    val title: String,
    val code: String,
    val explanation: String,
    val category: String,
    val generatedAt: Long = System.currentTimeMillis(),
    val usedCount: Int = 0
)
