package com.example.codesnack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AiTip::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aiTipDao(): AiTipDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codesnack_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
