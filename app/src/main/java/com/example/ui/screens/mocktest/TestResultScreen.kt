package com.example.ui.screens.mocktest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveTestSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultScreen(
    session: ActiveTestSession,
    onDone: () -> Unit
) {
    var filterTab by remember { mutableStateOf(0) } // 0: All, 1: Incorrect, 2: Correct, 3: Unattempted

    val totalQs = session.questions.size
    var correctCount = 0
    var incorrectCount = 0
    var unattemptedCount = 0
    var score = 0

    session.questions.forEach { q ->
        val ans = q.selectedAnswer.trim()
        val correct = q.question.correctAnswer.trim()
        if (ans.isEmpty()) {
            unattemptedCount++
        } else if (ans.equals(correct, ignoreCase = true)) {
            correctCount++
            score += 4
        } else {
            incorrectCount++
            score -= 1
        }
    }

    val totalMarks = totalQs * 4
    val accuracy = if (correctCount + incorrectCount > 0) (correctCount.toFloat() / (correctCount + incorrectCount) * 100f) else 0f
    val fraction = (score.toFloat() / totalMarks.coerceAtLeast(1)).coerceIn(0f, 1f)
    val percentile = when {
        fraction >= 0.85f -> 99.8f + (fraction - 0.85f) * 1.2f
        fraction >= 0.70f -> 98.5f + (fraction - 0.70f) * 8.6f
        fraction >= 0.50f -> 94.0f + (fraction - 0.50f) * 22.5f
        fraction >= 0.35f -> 85.0f + (fraction - 0.35f) * 60.0f
        else -> (fraction / 0.35f * 85.0f)
    }.coerceIn(10.0f, 99.99f)
    val rank = ((1f - (percentile / 100f)) * 1400000).toInt().coerceAtLeast(1)

    val displayedQuestions = remember(session.questions, filterTab) {
        session.questions.filterIndexed { _, q ->
            val ans = q.selectedAnswer.trim()
            val correct = q.question.correctAnswer.trim()
            when (filterTab) {
                1 -> ans.isNotEmpty() && !ans.equals(correct, ignoreCase = true)
                2 -> ans.isNotEmpty() && ans.equals(correct, ignoreCase = true)
                3 -> ans.isEmpty()
                else -> true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Test Analytics & Solutions", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(
                        onClick = onDone,
                        modifier = Modifier.testTag("result_done_btn")
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            // Main Score Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = session.testTitle,
                            style = MaterialTheme.typography.titleSmall,
                            color = Slate400,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$score / $totalMarks",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (score >= 0) MaterialTheme.colorScheme.primary else ErrorRose
                        )

                        Text(
                            text = "Total Score (Marking: +4 for Correct, -1 for Incorrect)",
                            fontSize = 11.sp,
                            color = Slate400
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Percentile", fontSize = 11.sp, color = Slate400)
                                Text(
                                    text = "%.2f %%ile".format(percentile),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChemEmeraldLight
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Estimated AIR", fontSize = 11.sp, color = Slate400)
                                Text(
                                    text = "~$rank",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MathAmberLight
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Accuracy", fontSize = 11.sp, color = Slate400)
                                Text(
                                    text = "%.1f%%".format(accuracy),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Attempt breakdown bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SuccessGreen.copy(alpha = 0.2f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "✓ $correctCount Correct",
                                    color = SuccessGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ErrorRose.copy(alpha = 0.2f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "✗ $incorrectCount Wrong",
                                    color = ErrorRose,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Slate800,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "○ $unattemptedCount Left",
                                    color = Slate400,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Filter Tabs for Review
            item {
                TabRow(
                    selectedTabIndex = filterTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = filterTab == 0,
                        onClick = { filterTab = 0 },
                        text = { Text("All (${totalQs})", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = filterTab == 1,
                        onClick = { filterTab = 1 },
                        text = { Text("Wrong (${incorrectCount})", fontSize = 12.sp, color = ErrorRose) }
                    )
                    Tab(
                        selected = filterTab == 2,
                        onClick = { filterTab = 2 },
                        text = { Text("Correct (${correctCount})", fontSize = 12.sp, color = SuccessGreen) }
                    )
                    Tab(
                        selected = filterTab == 3,
                        onClick = { filterTab = 3 },
                        text = { Text("Skipped (${unattemptedCount})", fontSize = 12.sp) }
                    )
                }
            }

            // Question solutions
            itemsIndexed(displayedQuestions) { idx, q ->
                val ans = q.selectedAnswer.trim()
                val correct = q.question.correctAnswer.trim()
                val isCorrect = ans.isNotEmpty() && ans.equals(correct, ignoreCase = true)
                val isWrong = ans.isNotEmpty() && !ans.equals(correct, ignoreCase = true)
                val isSkipped = ans.isEmpty()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            when {
                                isCorrect -> SuccessGreen.copy(alpha = 0.5f)
                                isWrong -> ErrorRose.copy(alpha = 0.5f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Q. ${idx + 1} • ${q.question.chapter}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when {
                                    isCorrect -> SuccessGreen.copy(alpha = 0.15f)
                                    isWrong -> ErrorRose.copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ) {
                                Text(
                                    text = when {
                                        isCorrect -> "+4 Marks"
                                        isWrong -> "-1 Mark"
                                        else -> "0 Marks"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isCorrect -> SuccessGreen
                                        isWrong -> ErrorRose
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = q.question.questionText,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column {
                                Text("Your Answer:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (ans.isEmpty()) "Not Attempted" else ans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = when {
                                        isCorrect -> SuccessGreen
                                        isWrong -> ErrorRose
                                        else -> Slate400
                                    }
                                )
                            }

                            Column {
                                Text("Correct Answer:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = correct,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SuccessGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Step by Step Explanation Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Detailed Solution & Derivation:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = q.question.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
