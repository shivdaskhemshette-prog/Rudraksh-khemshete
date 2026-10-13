package com.example.data.repository

import com.example.data.DefaultJeeData
import com.example.data.local.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class JeeRepository(private val db: AppDatabase) {

    val studySlots: Flow<List<StudySlotEntity>> = db.studySlotDao().getAllSlots()
    val testResults: Flow<List<TestResultEntity>> = db.testResultDao().getAllResults()
    val questions: Flow<List<QuestionEntity>> = db.questionDao().getAllQuestions()
    val formulas: Flow<List<FormulaEntity>> = db.formulaDao().getAllFormulas()
    val focusSessions: Flow<List<FocusSessionEntity>> = db.focusSessionDao().getAllSessions()
    val studentProfile: Flow<StudentProfileEntity?> = db.studentProfileDao().getProfile()

    suspend fun checkAndSeedInitialData() {
        if (db.questionDao().getCount() == 0) {
            db.questionDao().insertQuestions(DefaultJeeData.getInitialQuestions())
        }
        if (db.formulaDao().getCount() == 0) {
            db.formulaDao().insertFormulas(DefaultJeeData.getInitialFormulas())
        }
        val currentProfile = db.studentProfileDao().getProfile().firstOrNull()
        if (currentProfile == null) {
            db.studentProfileDao().setProfile(DefaultJeeData.getInitialProfile())
        }
        val existingSlots = db.studySlotDao().getAllSlots().firstOrNull()
        if (existingSlots.isNullOrEmpty()) {
            db.studySlotDao().insertSlots(DefaultJeeData.getInitialSchedule())
        }
        val existingTests = db.testResultDao().getAllResults().firstOrNull()
        if (existingTests.isNullOrEmpty()) {
            DefaultJeeData.getInitialTestResults().forEach {
                db.testResultDao().insertResult(it)
            }
        }
    }

    // Schedule actions
    suspend fun toggleSlotCompletion(slotId: Long, isCompleted: Boolean) {
        db.studySlotDao().updateCompletion(slotId, isCompleted)
    }

    suspend fun addCustomSlot(slot: StudySlotEntity) {
        db.studySlotDao().insertSlot(slot)
    }

    suspend fun regenerateSchedule(dailyGoalHours: Int, focusSubject: String?) {
        db.studySlotDao().clearAll()
        val defaultList = DefaultJeeData.getInitialSchedule()
        db.studySlotDao().insertSlots(defaultList)
    }

    // Question actions
    suspend fun toggleQuestionBookmark(id: Long, isBookmarked: Boolean) {
        db.questionDao().toggleBookmark(id, isBookmarked)
    }

    suspend fun toggleQuestionMastered(id: Long, isMastered: Boolean) {
        db.questionDao().toggleMastered(id, isMastered)
    }

    // Formula actions
    suspend fun toggleFormulaBookmark(id: Long, isBookmarked: Boolean) {
        db.formulaDao().toggleBookmark(id, isBookmarked)
    }

    // Test actions
    suspend fun saveTestResult(result: TestResultEntity): Long {
        return db.testResultDao().insertResult(result)
    }

    suspend fun getTestResultById(id: Long): TestResultEntity? {
        return db.testResultDao().getResultById(id)
    }

    // Focus session actions
    suspend fun logFocusSession(session: FocusSessionEntity) {
        db.focusSessionDao().insertSession(session)
        db.studentProfileDao().addFocusMinutes(session.durationMinutes)
    }

    // Profile actions
    suspend fun updateProfile(profile: StudentProfileEntity) {
        db.studentProfileDao().setProfile(profile)
    }
}
