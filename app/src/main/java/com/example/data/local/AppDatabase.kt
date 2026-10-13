package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        StudySlotEntity::class,
        TestResultEntity::class,
        QuestionEntity::class,
        FormulaEntity::class,
        FocusSessionEntity::class,
        StudentProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studySlotDao(): StudySlotDao
    abstract fun testResultDao(): TestResultDao
    abstract fun questionDao(): QuestionDao
    abstract fun formulaDao(): FormulaDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun studentProfileDao(): StudentProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jee_ace_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
