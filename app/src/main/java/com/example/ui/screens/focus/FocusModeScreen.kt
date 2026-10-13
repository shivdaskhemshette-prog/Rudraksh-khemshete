package com.example.ui.screens.focus

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.JeeViewModel
import kotlin.math.sin

@Composable
fun FocusModeScreen(
    viewModel: JeeViewModel,
    onBack: () -> Unit
) {
    val remainingSeconds by viewModel.focusTimerSeconds.collectAsState()
    val initialSeconds by viewModel.focusInitialSeconds.collectAsState()
    val isRunning by viewModel.isFocusRunning.collectAsState()
    val selectedSubject by viewModel.focusSelectedSubject.collectAsState()
    val ambientVibe by viewModel.focusAmbientVibe.collectAsState()

    val progress = if (initialSeconds > 0) (remainingSeconds.toFloat() / initialSeconds.toFloat()) else 1f

    val quotes = remember {
        listOf(
            "“AIR 1 is forged in the silence of deep work.”",
            "“One concept at a time. Precision beats rush.”",
            "“The pain of discipline is far less than the pain of regret.”",
            "“Every problem solved with clarity is a step inside IIT.”",
            "“Eliminate distractions. Your future rank demands full presence.”"
        )
    }

    var quoteIndex by remember { mutableStateOf(0) }
    LaunchedEffect(isRunning) {
        if (isRunning) {
            quoteIndex = (quoteIndex + 1) % quotes.size
        }
    }

    // Smooth breathing animation for ambient focus aura
    val infiniteTransition = rememberInfiniteTransition(label = "focusAura")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraScale"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    // Full screen pure focus environment
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("focus_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Focus Mode",
                        tint = Slate400
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Slate900,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            if (isRunning) listOf(PhysicsCyan, AccentIndigo) else listOf(Slate800, Slate700)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isRunning) SuccessGreen else Slate600)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRunning) "Deep Focus Zone" else "Zero Distraction Mode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRunning) PhysicsCyan else Slate400
                        )
                    }
                }

                IconButton(
                    onClick = { quoteIndex = (quoteIndex + 1) % quotes.size }
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "New Quote",
                        tint = MathAmberLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preset Mode Switcher (only when paused/not running)
            if (!isRunning) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(
                        180 to "3h Exam Sim",
                        50 to "50m Pomodoro",
                        25 to "25m Sprint"
                    ).forEach { (mins, label) ->
                        val isPresetActive = (initialSeconds / 60) == mins
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isPresetActive) Slate850 else Slate900,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = if (isPresetActive) PhysicsGradient else Brush.linearGradient(listOf(Slate800, Slate800))
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setFocusPreset(mins) }
                                .testTag("focus_preset_${mins}m")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isPresetActive) FontWeight.Bold else FontWeight.Medium,
                                color = if (isPresetActive) PhysicsCyan else Slate400,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Glowing Focus Dial with Animated Aura
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(270.dp)
            ) {
                // Background ambient glowing aura
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (isRunning) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(PhysicsCyan.copy(alpha = 0.12f), Color.Transparent),
                                center = center,
                                radius = size.minDimension / 2 * auraScale
                            )
                        )
                    }
                }

                // Progress dial
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 10.dp,
                    color = PhysicsCyan,
                    trackColor = Slate800
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatDigitalTimer(remainingSeconds),
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        modifier = Modifier.testTag("focus_timer_text")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SubjectBadge(subjectName = selectedSubject)

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ambientVibe,
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            // Animated Ambient Waveform when running
            if (isRunning) {
                Spacer(modifier = Modifier.height(12.dp))
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val midY = height / 2

                    val path = Path().apply {
                        moveTo(0f, midY)
                        var x = 0f
                        while (x <= width) {
                            val y = midY + (sin(x * 0.03f + wavePhase) * 8f)
                            lineTo(x, y)
                            x += 4f
                        }
                    }

                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, PhysicsCyan.copy(alpha = 0.8f), AccentIndigo, Color.Transparent)
                        ),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inspiring JEE Quote Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(WarningGold.copy(alpha = 0.25f), Color.Transparent)
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = quotes[quoteIndex],
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Slate200,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ambient sound & subject selectors (when not running)
            if (!isRunning) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Subject chooser
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Target Subject:", fontSize = 12.sp, color = Slate400)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Physics", "Chemistry", "Mathematics", "Revision").forEach { subj ->
                                val isSelected = selectedSubject == subj
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Slate850 else Slate900,
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = if (isSelected) PhysicsGradient else Brush.linearGradient(listOf(Slate800, Slate800))
                                    ),
                                    modifier = Modifier.clickable { viewModel.setFocusSubject(subj) }
                                ) {
                                    Text(
                                        text = subj.take(4),
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) PhysicsCyan else Slate400,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Ambience chooser
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Atmosphere:", fontSize = 12.sp, color = Slate400)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Binaural 40Hz", "Rain", "Library", "Silence").forEach { vibe ->
                                val isSelected = ambientVibe.contains(vibe, ignoreCase = true)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Slate850 else Slate900,
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = if (isSelected) PhysicsGradient else Brush.linearGradient(listOf(Slate800, Slate800))
                                    ),
                                    modifier = Modifier.clickable { viewModel.setFocusAmbient(vibe) }
                                ) {
                                    Text(
                                        text = vibe,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) PhysicsCyan else Slate400,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRunning) {
                    FilledTonalButton(
                        onClick = { viewModel.pauseFocusTimer() },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Slate800, contentColor = Color.White),
                        modifier = Modifier.testTag("focus_pause_button")
                    ) {
                        Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pause")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { viewModel.finishFocusSessionEarly() },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.testTag("focus_complete_early_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Done", tint = Slate950)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Log Session", color = Slate950, fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(
                        onClick = { viewModel.resetFocusTimer() },
                        modifier = Modifier.testTag("focus_reset_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { viewModel.startFocusTimer() },
                        colors = ButtonDefaults.buttonColors(containerColor = PhysicsCyan),
                        modifier = Modifier
                            .height(52.dp)
                            .padding(horizontal = 12.dp)
                            .testTag("focus_start_button")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", tint = Slate950)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enter Focus Zone",
                            color = Slate950,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

fun formatDigitalTimer(seconds: Int): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hrs > 0) {
        "%02d:%02d:%02d".format(hrs, mins, secs)
    } else {
        "%02d:%02d".format(mins, secs)
    }
}
