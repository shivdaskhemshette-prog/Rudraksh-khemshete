package com.example.ui.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DefaultJeeData
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.JeeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppDestination(val label: String) {
    SCHEDULE("Schedule"),
    MOCK_TESTS("Mock Tests"),
    QUESTION_BANK("Question Bank"),
    FORMULAS("Formulas"),
    ANALYTICS("Analytics"),
    FOCUS_MODE("Focus Timer")
}

data class ActiveTestQuestion(
    val id: Long,
    val question: QuestionEntity,
    val selectedAnswer: String = "",
    val isMarkedForReview: Boolean = false,
    val timeSpentSeconds: Int = 0
)

data class ActiveTestSession(
    val testId: String,
    val testTitle: String,
    val totalTimeSeconds: Int,
    val remainingSeconds: Int,
    val questions: List<ActiveTestQuestion>,
    val currentIndex: Int = 0,
    val isSubmitted: Boolean = false,
    val savedResultId: Long? = null
)

class JeeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JeeRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = JeeRepository(db)
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppDestination.SCHEDULE)
    val currentScreen: StateFlow<AppDestination> = _currentScreen.asStateFlow()

    fun navigateTo(dest: AppDestination) {
        _currentScreen.value = dest
    }

    // Repository Flows
    val studySlots: StateFlow<List<StudySlotEntity>> = repository.studySlots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val testResults: StateFlow<List<TestResultEntity>> = repository.testResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val questions: StateFlow<List<QuestionEntity>> = repository.questions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val formulas: StateFlow<List<FormulaEntity>> = repository.formulas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profile: StateFlow<StudentProfileEntity?> = repository.studentProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Schedule Screen State
    private val _selectedDay = MutableStateFlow(1) // 1 (Mon) to 7 (Sun)
    val selectedDay: StateFlow<Int> = _selectedDay.asStateFlow()

    fun selectDay(day: Int) {
        _selectedDay.value = day
    }

    fun toggleSlotCompletion(slotId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleSlotCompletion(slotId, isCompleted)
        }
    }

    fun addNewStudySlot(day: Int, time: String, subject: String, topic: String, duration: Int) {
        viewModelScope.launch {
            repository.addCustomSlot(
                StudySlotEntity(
                    dayOfWeek = day,
                    timeSlot = time,
                    subject = subject,
                    topic = topic,
                    durationMinutes = duration
                )
            )
        }
    }

    fun regeneratePersonalizedSchedule(hours: Int) {
        viewModelScope.launch {
            repository.regenerateSchedule(hours, null)
        }
    }

    // Question Bank Filters
    private val _selectedSubjectFilter = MutableStateFlow("ALL")
    val selectedSubjectFilter: StateFlow<String> = _selectedSubjectFilter.asStateFlow()

    private val _selectedDifficultyFilter = MutableStateFlow("ALL")
    val selectedDifficultyFilter: StateFlow<String> = _selectedDifficultyFilter.asStateFlow()

    private val _questionSearchQuery = MutableStateFlow("")
    val questionSearchQuery: StateFlow<String> = _questionSearchQuery.asStateFlow()

    fun setSubjectFilter(filter: String) { _selectedSubjectFilter.value = filter }
    fun setDifficultyFilter(filter: String) { _selectedDifficultyFilter.value = filter }
    fun setQuestionSearch(query: String) { _questionSearchQuery.value = query }

    fun toggleQuestionBookmark(questionId: Long, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleQuestionBookmark(questionId, isBookmarked)
        }
    }

    fun toggleQuestionMastered(questionId: Long, isMastered: Boolean) {
        viewModelScope.launch {
            repository.toggleQuestionMastered(questionId, isMastered)
        }
    }

    // Formula Filters
    private val _formulaSubjectFilter = MutableStateFlow("ALL")
    val formulaSubjectFilter: StateFlow<String> = _formulaSubjectFilter.asStateFlow()

    private val _formulaSearchQuery = MutableStateFlow("")
    val formulaSearchQuery: StateFlow<String> = _formulaSearchQuery.asStateFlow()

    fun setFormulaSubjectFilter(filter: String) { _formulaSubjectFilter.value = filter }
    fun setFormulaSearch(query: String) { _formulaSearchQuery.value = query }

    fun toggleFormulaBookmark(formulaId: Long, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleFormulaBookmark(formulaId, isBookmarked)
        }
    }

    // Mock Test Execution State
    private val _activeTestSession = MutableStateFlow<ActiveTestSession?>(null)
    val activeTestSession: StateFlow<ActiveTestSession?> = _activeTestSession.asStateFlow()

    private var testCountDownTimer: CountDownTimer? = null

    fun startTest(testTitle: String, durationMinutes: Int, questionsList: List<QuestionEntity>) {
        val testQuestions = (if (questionsList.isNotEmpty()) questionsList else DefaultJeeData.getInitialQuestions())
            .map { ActiveTestQuestion(id = it.id, question = it) }

        val totalSecs = durationMinutes * 60
        _activeTestSession.value = ActiveTestSession(
            testId = "mock_${System.currentTimeMillis()}",
            testTitle = testTitle,
            totalTimeSeconds = totalSecs,
            remainingSeconds = totalSecs,
            questions = testQuestions,
            currentIndex = 0
        )

        testCountDownTimer?.cancel()
        testCountDownTimer = object : CountDownTimer(totalSecs * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                _activeTestSession.update { current ->
                    current?.copy(remainingSeconds = (millisUntilFinished / 1000).toInt())
                }
            }

            override fun onFinish() {
                submitTest()
            }
        }.start()
    }

    fun selectQuestionIndex(index: Int) {
        _activeTestSession.update { it?.copy(currentIndex = index) }
    }

    fun answerCurrentQuestion(answer: String) {
        _activeTestSession.update { session ->
            if (session == null) return@update null
            val updated = session.questions.toMutableList()
            val curr = updated[session.currentIndex]
            updated[session.currentIndex] = curr.copy(selectedAnswer = answer)
            session.copy(questions = updated)
        }
    }

    fun clearCurrentResponse() {
        _activeTestSession.update { session ->
            if (session == null) return@update null
            val updated = session.questions.toMutableList()
            val curr = updated[session.currentIndex]
            updated[session.currentIndex] = curr.copy(selectedAnswer = "")
            session.copy(questions = updated)
        }
    }

    fun toggleMarkForReview() {
        _activeTestSession.update { session ->
            if (session == null) return@update null
            val updated = session.questions.toMutableList()
            val curr = updated[session.currentIndex]
            updated[session.currentIndex] = curr.copy(isMarkedForReview = !curr.isMarkedForReview)
            session.copy(questions = updated)
        }
    }

    fun nextQuestion() {
        _activeTestSession.update { session ->
            if (session == null) return@update null
            val nextIdx = (session.currentIndex + 1).coerceAtMost(session.questions.lastIndex)
            session.copy(currentIndex = nextIdx)
        }
    }

    fun prevQuestion() {
        _activeTestSession.update { session ->
            if (session == null) return@update null
            val prevIdx = (session.currentIndex - 1).coerceAtLeast(0)
            session.copy(currentIndex = prevIdx)
        }
    }

    fun submitTest() {
        testCountDownTimer?.cancel()
        val session = _activeTestSession.value ?: return
        if (session.isSubmitted) return

        var correct = 0
        var incorrect = 0
        var unattempted = 0
        var scoredMarks = 0

        session.questions.forEach { q ->
            val ans = q.selectedAnswer.trim()
            if (ans.isEmpty()) {
                unattempted++
            } else if (ans.equals(q.question.correctAnswer.trim(), ignoreCase = true)) {
                correct++
                scoredMarks += 4 // JEE marking +4
            } else {
                incorrect++
                scoredMarks -= 1 // JEE marking -1
            }
        }

        val totalMarks = session.questions.size * 4
        val accuracy = if (correct + incorrect > 0) (correct.toFloat() / (correct + incorrect) * 100f) else 0f
        val timeSpent = (session.totalTimeSeconds - session.remainingSeconds).toLong()

        // Percentile estimation algorithm based on standard JEE statistical curves
        val fraction = (scoredMarks.toFloat() / totalMarks.coerceAtLeast(1)).coerceIn(0f, 1f)
        val estimatedPercentile = when {
            fraction >= 0.85f -> 99.8f + (fraction - 0.85f) * 1.2f
            fraction >= 0.70f -> 98.5f + (fraction - 0.70f) * 8.6f
            fraction >= 0.50f -> 94.0f + (fraction - 0.50f) * 22.5f
            fraction >= 0.35f -> 85.0f + (fraction - 0.35f) * 60.0f
            else -> (fraction / 0.35f * 85.0f)
        }.coerceIn(10.0f, 99.99f)

        val estimatedRank = ((1f - (estimatedPercentile / 100f)) * 1400000).toInt().coerceAtLeast(1)

        val resultEntity = TestResultEntity(
            testId = session.testId,
            testTitle = session.testTitle,
            testType = "FULL_MOCK",
            totalMarks = totalMarks,
            scoredMarks = scoredMarks,
            correctCount = correct,
            incorrectCount = incorrect,
            unattemptedCount = unattempted,
            accuracy = accuracy,
            percentileEstimate = estimatedPercentile,
            rankEstimate = estimatedRank,
            timeTakenSeconds = timeSpent,
            completedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            val resultId = repository.saveTestResult(resultEntity)
            _activeTestSession.update {
                it?.copy(isSubmitted = true, savedResultId = resultId)
            }
        }
    }

    fun exitTestSession() {
        testCountDownTimer?.cancel()
        _activeTestSession.value = null
    }

    // Focus Mode Timer State
    private val _focusTimerSeconds = MutableStateFlow(180 * 60) // Default 3 hours exam simulation
    val focusTimerSeconds: StateFlow<Int> = _focusTimerSeconds.asStateFlow()

    private val _focusInitialSeconds = MutableStateFlow(180 * 60)
    val focusInitialSeconds: StateFlow<Int> = _focusInitialSeconds.asStateFlow()

    private val _isFocusRunning = MutableStateFlow(false)
    val isFocusRunning: StateFlow<Boolean> = _isFocusRunning.asStateFlow()

    private val _focusSelectedSubject = MutableStateFlow("Physics")
    val focusSelectedSubject: StateFlow<String> = _focusSelectedSubject.asStateFlow()

    private val _focusAmbientVibe = MutableStateFlow("Binaural Beats (40Hz)")
    val focusAmbientVibe: StateFlow<String> = _focusAmbientVibe.asStateFlow()

    private var focusCountDownTimer: CountDownTimer? = null

    fun setFocusPreset(minutes: Int) {
        pauseFocusTimer()
        val secs = minutes * 60
        _focusInitialSeconds.value = secs
        _focusTimerSeconds.value = secs
    }

    fun setFocusSubject(subject: String) {
        _focusSelectedSubject.value = subject
    }

    fun setFocusAmbient(vibe: String) {
        _focusAmbientVibe.value = vibe
    }

    fun startFocusTimer() {
        if (_isFocusRunning.value) return
        _isFocusRunning.value = true
        val currentSecs = _focusTimerSeconds.value

        focusCountDownTimer = object : CountDownTimer(currentSecs * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                _focusTimerSeconds.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                _isFocusRunning.value = false
                val completedMins = _focusInitialSeconds.value / 60
                viewModelScope.launch {
                    repository.logFocusSession(
                        FocusSessionEntity(
                            mode = if (completedMins >= 180) "EXAM_3HR" else "POMODORO",
                            durationMinutes = completedMins,
                            subject = _focusSelectedSubject.value
                        )
                    )
                }
            }
        }.start()
    }

    fun pauseFocusTimer() {
        _isFocusRunning.value = false
        focusCountDownTimer?.cancel()
    }

    fun resetFocusTimer() {
        pauseFocusTimer()
        _focusTimerSeconds.value = _focusInitialSeconds.value
    }

    fun finishFocusSessionEarly() {
        val elapsedSecs = _focusInitialSeconds.value - _focusTimerSeconds.value
        val elapsedMins = (elapsedSecs / 60).coerceAtLeast(1)
        pauseFocusTimer()
        viewModelScope.launch {
            repository.logFocusSession(
                FocusSessionEntity(
                    mode = "CUSTOM",
                    durationMinutes = elapsedMins,
                    subject = _focusSelectedSubject.value
                )
            )
            resetFocusTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        testCountDownTimer?.cancel()
        focusCountDownTimer?.cancel()
    }
}
