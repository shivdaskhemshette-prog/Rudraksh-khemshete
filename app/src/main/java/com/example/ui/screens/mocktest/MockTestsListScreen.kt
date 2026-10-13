package com.example.ui.screens.mocktest

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
import com.example.data.model.TestResultEntity
import com.example.ui.components.JeeTopHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.JeeViewModel
import java.text.SimpleDateFormat
import java.util.*

data class AvailableMockTest(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val totalMarks: Int,
    val questionCount: Int,
    val subject: String,
    val difficulty: String
)

@Composable
fun MockTestsListScreen(
    viewModel: JeeViewModel,
    onStartTest: (title: String, durationMinutes: Int, questions: List<QuestionEntity>) -> Unit,
    onNavigateToFocus: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val allQuestions by viewModel.questions.collectAsState()
    val pastResults by viewModel.testResults.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Available Tests, 1: Past Attempts

    val mockTests = remember {
        listOf(
            AvailableMockTest(
                id = "full_mock_1",
                title = "JEE Main 2026 Full Syllabus All-India Mock #1",
                description = "Balanced NTA pattern test covering Physics, Chemistry, and Mathematics with Section A (MCQs) & Section B (Numericals).",
                durationMinutes = 180,
                totalMarks = 300,
                questionCount = 13,
                subject = "ALL",
                difficulty = "JEE Main"
            ),
            AvailableMockTest(
                id = "phys_part_1",
                title = "Physics Mastery: Mechanics & Electrodynamics Part Test",
                description = "High-weightage questions on Rotational Dynamics, Gauss Law, and Capacitors with detailed solutions.",
                durationMinutes = 60,
                totalMarks = 100,
                questionCount = 5,
                subject = "PHYSICS",
                difficulty = "JEE Adv / Main"
            ),
            AvailableMockTest(
                id = "chem_part_1",
                title = "Chemistry High-Yield: Organic Reactions & Kinetics Test",
                description = "Speed booster on Named Reactions, Coordination Compounds, and Chemical Equilibrium.",
                durationMinutes = 60,
                totalMarks = 100,
                questionCount = 4,
                subject = "CHEMISTRY",
                difficulty = "JEE Main"
            ),
            AvailableMockTest(
                id = "math_part_1",
                title = "Mathematics Sprint: Calculus & Vectors Advanced Drill",
                description = "Challenging multi-concept problems on Definite Integrals, Conics tangents, and 3D skew lines.",
                durationMinutes = 60,
                totalMarks = 100,
                questionCount = 4,
                subject = "MATHEMATICS",
                difficulty = "JEE Advanced"
            ),
            AvailableMockTest(
                id = "speed_pyq_1",
                title = "15-Minute Daily Speed & Accuracy Diagnostic",
                description = "Rapid fire PYQs to test problem-solving speed under high exam time pressure.",
                durationMinutes = 15,
                totalMarks = 40,
                questionCount = 6,
                subject = "ALL",
                difficulty = "Speed Drill"
            )
        )
    }

    Scaffold(
        topBar = {
            JeeTopHeader(
                title = "Mock Tests & CBT",
                subtitle = "Real Exam Simulation • Marking: +4 / -1",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Tab Switcher
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Available Tests", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("tab_available_tests")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Attempt History (${pastResults.size})", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("tab_past_tests")
                    )
                }
            }

            if (selectedTab == 0) {
                // Exam Instructions Banner
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Slate850),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = PhysicsCyan,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Authentic CBT Exam Interface",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Includes live countdown timer, question status palette, Section A & B, and instant rank & percentile analytics upon submission.",
                                    fontSize = 12.sp,
                                    color = Slate400
                                )
                            }
                        }
                    }
                }

                items(mockTests, key = { it.id }) { test ->
                    MockTestCard(
                        test = test,
                        onStart = {
                            val testQuestions = when (test.subject) {
                                "PHYSICS" -> allQuestions.filter { it.subject == "PHYSICS" }
                                "CHEMISTRY" -> allQuestions.filter { it.subject == "CHEMISTRY" }
                                "MATHEMATICS" -> allQuestions.filter { it.subject == "MATHEMATICS" }
                                else -> allQuestions
                            }
                            onStartTest(test.title, test.durationMinutes, testQuestions)
                        }
                    )
                }
            } else {
                if (pastResults.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assessment,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No tests attempted yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Take an available mock test above to see detailed accuracy, rank & percentile analytics.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(pastResults, key = { it.id }) { result ->
                        PastTestResultCard(result = result)
                    }
                }
            }
        }
    }
}

@Composable
fun MockTestCard(
    test: AvailableMockTest,
    onStart: () -> Unit
) {
    val isFullMock = test.subject == "ALL"
    val borderBrush = if (isFullMock) {
        Brush.linearGradient(
            listOf(PhysicsCyan.copy(alpha = 0.6f), AccentIndigo.copy(alpha = 0.4f), Color.Transparent)
        )
    } else {
        Brush.linearGradient(listOf(Slate800, Slate800))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mock_test_card_${test.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = CardDefaults.outlinedCardBorder().copy(brush = borderBrush)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (test.subject) {
                        "PHYSICS" -> PhysicsCyan.copy(alpha = 0.15f)
                        "CHEMISTRY" -> ChemEmerald.copy(alpha = 0.15f)
                        "MATHEMATICS" -> MathAmber.copy(alpha = 0.15f)
                        else -> AccentIndigo.copy(alpha = 0.18f)
                    },
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = when (test.subject) {
                            "PHYSICS" -> PhysicsGradient
                            "CHEMISTRY" -> ChemGradient
                            "MATHEMATICS" -> MathGradient
                            else -> Brush.horizontalGradient(listOf(AccentIndigo, AccentPurple))
                        }
                    )
                ) {
                    Text(
                        text = if (test.subject == "ALL") "★ All-India Full Mock" else test.subject,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (test.subject) {
                            "PHYSICS" -> PhysicsCyan
                            "CHEMISTRY" -> ChemEmeraldLight
                            "MATHEMATICS" -> MathAmberLight
                            else -> Color(0xFFA5B4FC)
                        }
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate850
                ) {
                    Text(
                        text = test.difficulty,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = test.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = test.description,
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = PhysicsCyan
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${test.durationMinutes} mins",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate200
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = WarningGold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${test.totalMarks} Marks",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = WarningGold
                        )
                    }
                }

                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = PhysicsCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("start_test_btn_${test.id}")
                ) {
                    Text(
                        text = "Enter Exam",
                        color = Slate950,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PastTestResultCard(result: TestResultEntity) {
    val dateStr = remember(result.completedAt) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(result.completedAt))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.testTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Score", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${result.scoredMarks} / ${result.totalMarks}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (result.scoredMarks >= 0) MaterialTheme.colorScheme.primary else ErrorRose
                    )
                }

                Column {
                    Text(text = "Percentile", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "%.2f %%ile".format(result.percentileEstimate),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = ChemEmeraldLight
                    )
                }

                Column {
                    Text(text = "Est. AIR", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "~${result.rankEstimate}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MathAmberLight
                    )
                }

                Column {
                    Text(text = "Accuracy", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "%.1f%%".format(result.accuracy),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Breakdown pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SuccessGreen.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "✓ ${result.correctCount} Correct",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ErrorRose.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "✗ ${result.incorrectCount} Wrong",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ErrorRose,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "○ ${result.unattemptedCount} Skipped",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                    )
                }
            }
        }
    }
}
