package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySlotDao {
    @Query("SELECT * FROM study_slots ORDER BY id ASC")
    fun getAllSlots(): Flow<List<StudySlotEntity>>

    @Query("SELECT * FROM study_slots WHERE dayOfWeek = :dayOfWeek ORDER BY id ASC")
    fun getSlotsForDay(dayOfWeek: Int): Flow<List<StudySlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlots(slots: List<StudySlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlot(slot: StudySlotEntity)

    @Update
    suspend fun updateSlot(slot: StudySlotEntity)

    @Query("UPDATE study_slots SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletion(id: Long, isCompleted: Boolean)

    @Query("DELETE FROM study_slots")
    suspend fun clearAll()
}

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results ORDER BY completedAt DESC")
    fun getAllResults(): Flow<List<TestResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: TestResultEntity): Long

    @Query("SELECT * FROM test_results WHERE id = :id LIMIT 1")
    suspend fun getResultById(id: Long): TestResultEntity?

    @Query("SELECT COUNT(*) FROM test_results")
    fun getCompletedTestCount(): Flow<Int>
}

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subject = :subject ORDER BY id ASC")
    fun getQuestionsBySubject(subject: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isBookmarked = 1 ORDER BY id ASC")
    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("UPDATE questions SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: Long, isBookmarked: Boolean)

    @Query("UPDATE questions SET isMastered = :isMastered WHERE id = :id")
    suspend fun toggleMastered(id: Long, isMastered: Boolean)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getCount(): Int
}

@Dao
interface FormulaDao {
    @Query("SELECT * FROM formulas ORDER BY id ASC")
    fun getAllFormulas(): Flow<List<FormulaEntity>>

    @Query("SELECT * FROM formulas WHERE subject = :subject ORDER BY id ASC")
    fun getFormulasBySubject(subject: String): Flow<List<FormulaEntity>>

    @Query("SELECT * FROM formulas WHERE isBookmarked = 1 ORDER BY id ASC")
    fun getBookmarkedFormulas(): Flow<List<FormulaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulas(formulas: List<FormulaEntity>)

    @Query("UPDATE formulas SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: Long, isBookmarked: Boolean)

    @Query("SELECT COUNT(*) FROM formulas")
    suspend fun getCount(): Int
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity)

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions")
    fun getTotalFocusMinutes(): Flow<Int?>
}

@Dao
interface StudentProfileDao {
    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<StudentProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProfile(profile: StudentProfileEntity)

    @Query("UPDATE student_profile SET totalFocusMinutes = totalFocusMinutes + :additionalMinutes WHERE id = 1")
    suspend fun addFocusMinutes(additionalMinutes: Int)

    @Query("UPDATE student_profile SET currentStreak = currentStreak + 1 WHERE id = 1")
    suspend fun incrementStreak()
}
