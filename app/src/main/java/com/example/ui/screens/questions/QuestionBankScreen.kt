package com.example.ui.screens.questions

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionEntity
import com.example.data.model.QuestionType
import com.example.ui.components.JeeTopHeader
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.JeeViewModel

@Composable
fun QuestionBankScreen(
    viewModel: JeeViewModel,
    onNavigateToFocus: () -> Unit
) {
    val allQuestions by viewModel.questions.collectAsState()
    val subjectFilter by viewModel.selectedSubjectFilter.collectAsState()
    val difficultyFilter by viewModel.selectedDifficultyFilter.collectAsState()
    val searchQuery by viewModel.questionSearchQuery.collectAsState()
    val profile by viewModel.profile.collectAsState()

    var showOnlyBookmarked by remember { mutableStateOf(false) }

    val filteredQuestions = remember(allQuestions, subjectFilter, difficultyFilter, searchQuery, showOnlyBookmarked) {
        allQuestions.filter { q ->
            val matchSubject = subjectFilter == "ALL" || q.subject.equals(subjectFilter, ignoreCase = true)
            val matchDifficulty = difficultyFilter == "ALL" || q.difficulty.equals(difficultyFilter, ignoreCase = true)
            val matchSearch = searchQuery.isBlank() ||
                    q.questionText.contains(searchQuery, ignoreCase = true) ||
                    q.chapter.contains(searchQuery, ignoreCase = true) ||
                    q.pyqYear.contains(searchQuery, ignoreCase = true)
            val matchBookmark = !showOnlyBookmarked || q.isBookmarked

            matchSubject && matchDifficulty && matchSearch && matchBookmark
        }
    }

    Scaffold(
        topBar = {
            JeeTopHeader(
                title = "Question Bank",
                subtitle = "${filteredQuestions.size} High-Yield JEE Questions",
                streakDays = profile?.currentStreak ?: 12,
                targetYear = profile?.targetYear ?: 2026,
                onFocusClick = onNavigateToFocus
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Search Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setQuestionSearch(it) },
                    modifier = Modifier.fillMaxWidth().testTag("search_question_bank_input"),
                    placeholder = { Text("Search by topic, chapter or PYQ year...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setQuestionSearch("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Subject Filter Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("ALL" to "All", "PHYSICS" to "Physics", "CHEMISTRY" to "Chemistry", "MATHEMATICS" to "Maths").forEach { (key, label) ->
                        FilterChip(
                            selected = subjectFilter == key,
                            onClick = { viewModel.setSubjectFilter(key) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("filter_subject_$key")
                        )
                    }
                }
            }

            // Secondary filter row (Difficulty & Bookmarks)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("ALL" to "All Levels", "MEDIUM" to "JEE Main", "TOUGH" to "JEE Adv").forEach { (key, label) ->
                            FilterChip(
                                selected = difficultyFilter == key,
                                onClick = { viewModel.setDifficultyFilter(key) },
                                label = { Text(label, fontSize = 10.sp) },
                                modifier = Modifier.testTag("filter_diff_$key")
                            )
                        }
                    }

                    FilterChip(
                        selected = showOnlyBookmarked,
                        onClick = { showOnlyBookmarked = !showOnlyBookmarked },
                        label = { Text("★ Saved", fontSize = 10.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = if (showOnlyBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        modifier = Modifier.testTag("filter_bookmark_toggle")
                    )
                }
            }

            if (filteredQuestions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("No matching questions found", fontWeight = FontWeight.Bold)
                            Text("Try clearing filters or search queries.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(filteredQuestions, key = { it.id }) { question ->
                    QuestionPracticeCard(
                        question = question,
                        onToggleBookmark = { viewModel.toggleQuestionBookmark(question.id, !question.isBookmarked) },
                        onToggleMastered = { viewModel.toggleQuestionMastered(question.id, !question.isMastered) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionPracticeCard(
    question: QuestionEntity,
    onToggleBookmark: () -> Unit,
    onToggleMastered: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("") }
    var showExplanation by remember { mutableStateOf(false) }

    val isAnswered = selectedOption.isNotBlank()
    val isCorrect = isAnswered && selectedOption.equals(question.correctAnswer.trim(), ignoreCase = true)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("question_card_${question.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(Slate800, Slate700.copy(alpha = 0.5f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SubjectBadge(subjectName = question.subject)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Slate850
                    ) {
                        Text(
                            text = question.chapter,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = Slate400
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleMastered, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (question.isMastered) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = "Mastered",
                            tint = if (question.isMastered) SuccessGreen else Slate600,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onToggleBookmark, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (question.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (question.isBookmarked) WarningGold else Slate600,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // PYQ badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = AccentIndigo.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "★ ${question.pyqYear}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA5B4FC),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question statement
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Options (if MCQ)
            if (question.questionType == QuestionType.MCQ.name) {
                val opts = listOf(
                    "A" to question.optionA,
                    "B" to question.optionB,
                    "C" to question.optionC,
                    "D" to question.optionD
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    opts.forEach { (key, optText) ->
                        val isChosen = selectedOption == key
                        val isRealCorrect = key.equals(question.correctAnswer.trim(), ignoreCase = true)

                        val optionBg = when {
                            showExplanation && isRealCorrect -> SuccessGreen.copy(alpha = 0.2f)
                            isChosen && !isRealCorrect && showExplanation -> ErrorRose.copy(alpha = 0.2f)
                            isChosen -> Slate850
                            else -> Slate950
                        }

                        val optionBorder = when {
                            showExplanation && isRealCorrect -> Brush.linearGradient(listOf(SuccessGreen, SuccessGreen))
                            isChosen && !isRealCorrect && showExplanation -> Brush.linearGradient(listOf(ErrorRose, ErrorRose))
                            isChosen -> PhysicsGradient
                            else -> Brush.linearGradient(listOf(Slate800, Slate800))
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedOption = key },
                            shape = RoundedCornerShape(10.dp),
                            color = optionBg,
                            border = CardDefaults.outlinedCardBorder().copy(brush = optionBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isChosen) PhysicsCyan else Slate800,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = key,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (isChosen) Slate950 else Slate400
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = optText,
                                    fontSize = 12.sp,
                                    color = if (isChosen) Color.White else Slate200,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate850,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Numerical Answer Problem • Correct Value: ${question.correctAnswer}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PhysicsCyan,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action row: Reveal Solution toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAnswered) {
                    Text(
                        text = if (isCorrect) "✓ Correct Answer!" else "✗ Check Solution",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect) SuccessGreen else ErrorRose
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                TextButton(
                    onClick = { showExplanation = !showExplanation },
                    modifier = Modifier.testTag("toggle_solution_${question.id}")
                ) {
                    Text(
                        text = if (showExplanation) "Hide Solution" else "View Full Solution",
                        fontWeight = FontWeight.Bold,
                        color = PhysicsCyan,
                        fontSize = 12.sp
                    )
                }
            }

            // Expandable Solution Card
            AnimatedVisibility(visible = showExplanation) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate850),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(SuccessGreen.copy(alpha = 0.4f), Color.Transparent))
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = PhysicsCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Correct Answer: Option ${question.correctAnswer}",
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate200,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
