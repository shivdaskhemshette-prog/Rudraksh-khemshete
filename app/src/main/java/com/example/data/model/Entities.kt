package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Subject(val displayName: String) {
    PHYSICS("Physics"),
    CHEMISTRY("Chemistry"),
    MATHEMATICS("Mathematics"),
    REVISION("Grand Revision"),
    MOCK_TEST("Mock Test")
}

enum class QuestionType {
    MCQ,
    NUMERICAL
}

enum class Difficulty(val label: String) {
    EASY("Foundation"),
    MEDIUM("JEE Main"),
    TOUGH("JEE Advanced")
}

@Entity(tableName = "study_slots")
data class StudySlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: Int, // 1 (Mon) to 7 (Sun)
    val timeSlot: String, // e.g. "06:00 - 08:30"
    val subject: String, // Subject.name
    val topic: String,
    val durationMinutes: Int = 120,
    val isCompleted: Boolean = false,
    val dateStr: String = "" // "YYYY-MM-DD"
)

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testId: String,
    val testTitle: String,
    val testType: String, // "FULL_MOCK", "PHYSICS_TEST", "CHEM_TEST", "MATH_TEST"
    val totalMarks: Int = 300,
    val scoredMarks: Int = 0,
    val correctCount: Int = 0,
    val incorrectCount: Int = 0,
    val unattemptedCount: Int = 0,
    val accuracy: Float = 0f,
    val percentileEstimate: Float = 0f,
    val rankEstimate: Int = 0,
    val timeTakenSeconds: Long = 0,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val chapter: String,
    val questionType: String = QuestionType.MCQ.name,
    val difficulty: String = Difficulty.MEDIUM.name,
    val questionText: String,
    val optionA: String = "",
    val optionB: String = "",
    val optionC: String = "",
    val optionD: String = "",
    val correctAnswer: String, // "A", "B", "C", "D" or integer string like "24"
    val explanation: String,
    val pyqYear: String = "JEE Main",
    val isBookmarked: Boolean = false,
    val isMastered: Boolean = false
)

@Entity(tableName = "formulas")
data class FormulaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val chapter: String,
    val title: String,
    val formulaText: String,
    val description: String,
    val variablesDef: String = "",
    val keyTip: String = "",
    val isBookmarked: Boolean = false
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mode: String, // "EXAM_3HR", "POMODORO", "CUSTOM"
    val durationMinutes: Int,
    val subject: String,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "student_profile")
data class StudentProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "JEE Aspirant",
    val targetYear: Int = 2026,
    val targetExam: String = "JEE Main & Advanced",
    val targetRank: Int = 1000,
    val dailyGoalHours: Int = 8,
    val currentStreak: Int = 14,
    val totalFocusMinutes: Int = 4200
)
