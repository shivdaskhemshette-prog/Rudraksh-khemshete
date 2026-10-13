package com.example.ui.screens.analytics

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.JeeTopHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.JeeViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalyticsScreen(
    viewModel: JeeViewModel,
    onNavigateToFocus: () -> Unit
) {
    val pastResults by viewModel.testResults.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val averageAccuracy = remember(pastResults) {
        if (pastResults.isNotEmpty()) pastResults.map { it.accuracy }.average().toFloat() else 81.5f
    }

    val latestPercentile = remember(pastResults) {
        pastResults.firstOrNull()?.percentileEstimate ?: 99.25f
    }

    val latestRank = remember(pastResults) {
        pastResults.firstOrNull()?.rankEstimate ?: 8500
    }

    Scaffold(
        topBar = {
            JeeTopHeader(
                title = "Performance Radar",
                subtitle = "AIR Predictor & Accuracy Indices",
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
            // Speedometer & NTA Percentile Gauge Hero
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(PhysicsCyan.copy(alpha = 0.6f), AccentIndigo.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NTA Percentile & AIR Forecast",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ChemEmerald.copy(alpha = 0.15f),
                                border = CardDefaults.outlinedCardBorder().copy(brush = ChemGradient)
                            ) {
                                Text(
                                    text = "Target: IIT Bombay",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ChemEmeraldLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Canvas Radial Gauge
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(200.dp)
                        ) {
                            RadialPercentileGauge(
                                percentile = latestPercentile,
                                modifier = Modifier.fillMaxSize()
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "%.2f".format(latestPercentile),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PhysicsCyan
                                )
                                Text(
                                    text = "PERCENTILE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate400,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Slate850
                                ) {
                                    Text(
                                        text = "AIR ~$latestRank",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MathAmberLight,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            MetricPill("Readiness", "89%", PhysicsCyan)
                            MetricPill("Avg Accuracy", "%.1f%%".format(averageAccuracy), SuccessGreen)
                            MetricPill("Target AIR", "< 1,000", WarningGold)
                        }
                    }
                }
            }

            // Score Progression Trend Canvas Graph
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(AccentIndigo.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Mock Score Progression",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "300-Mark Scale Trends Over Tests",
                                    fontSize = 11.sp,
                                    color = Slate400
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "+26 pts Growth",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Line chart on Canvas
                        val scores = remember(pastResults) {
                            if (pastResults.isNotEmpty()) {
                                pastResults.map { it.scoredMarks.toFloat().coerceAtLeast(0f) }.reversed()
                            } else {
                                listOf(165f, 182f, 195f, 218f)
                            }
                        }

                        ScoreTrendCanvas(
                            scores = scores,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )
                    }
                }
            }

            // PCM Triangle Balance Bar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "PCM Subject Equilibrium",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Balanced preparation ensures high percentile across all sections",
                            fontSize = 11.sp,
                            color = Slate400
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SubjectBalanceBar(
                            subject = "Physics",
                            color = PhysicsCyan,
                            percent = 86,
                            chapter = "High Yield: Rotational Motion & Gauss Law"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SubjectBalanceBar(
                            subject = "Chemistry",
                            color = ChemEmeraldLight,
                            percent = 92,
                            chapter = "High Yield: Aldol/Cannizzaro & Coordination CFT"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SubjectBalanceBar(
                            subject = "Mathematics",
                            color = MathAmberLight,
                            percent = 74,
                            chapter = "Focus Needed: Conic Tangents & Leibniz Rule"
                        )
                    }
                }
            }

            // Time Efficiency Analysis
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate850)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MathAmber.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = MathAmber,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Exam Time Allocation Strategy",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Chemistry: 45 mins • Physics: 60 mins • Mathematics: 75 mins.\nSpend max 2.2 minutes per MCQ to reserve time for numerical calculations.",
                                fontSize = 11.sp,
                                color = Slate400,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadialPercentileGauge(
    percentile: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 14.dp.toPx()
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
        val arcSize = Size(diameter, diameter)

        // Background track (240 degree sweep)
        drawArc(
            color = Slate800,
            startAngle = 150f,
            sweepAngle = 240f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Value arc with glowing gradient
        val sweepAngle = ((percentile / 100f) * 240f).coerceIn(0f, 240f)
        val gradient = Brush.sweepGradient(
            0.4f to PhysicsCyan,
            0.7f to AccentIndigo,
            1.0f to PhysicsCyanGlow
        )

        drawArc(
            brush = gradient,
            startAngle = 150f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ScoreTrendCanvas(
    scores: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (scores.isEmpty()) return@Canvas

        val maxScore = 300f
        val padding = 16.dp.toPx()
        val graphWidth = size.width - padding * 2
        val graphHeight = size.height - padding * 2

        val points = scores.mapIndexed { index, score ->
            val x = padding + (index.toFloat() / (scores.size - 1).coerceAtLeast(1)) * graphWidth
            val y = padding + graphHeight - (score / maxScore) * graphHeight
            Offset(x, y)
        }

        // Draw grid lines
        for (i in 0..3) {
            val y = padding + graphHeight * (i / 3f)
            drawLine(
                color = Slate800,
                start = Offset(padding, y),
                end = Offset(size.width - padding, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw curved path
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]
                val midX = (prev.x + curr.x) / 2
                cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
            }
        }

        // Fill area under curve
        val fillPath = Path().apply {
            addPath(path)
            lineTo(points.last().x, size.height - padding)
            lineTo(points.first().x, size.height - padding)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                listOf(PhysicsCyan.copy(alpha = 0.25f), Color.Transparent)
            )
        )

        // Draw line
        drawPath(
            path = path,
            color = PhysicsCyan,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw point dots
        points.forEach { pt ->
            drawCircle(
                color = Slate950,
                radius = 6.dp.toPx(),
                center = pt
            )
            drawCircle(
                color = PhysicsCyan,
                radius = 4.dp.toPx(),
                center = pt
            )
        }
    }
}

@Composable
fun SubjectBalanceBar(
    subject: String,
    color: Color,
    percent: Int,
    chapter: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = subject,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }

            Text(
                text = "$percent%",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Slate800
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = chapter,
            fontSize = 10.sp,
            color = Slate400
        )
    }
}

@Composable
fun MetricPill(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = Slate400)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
