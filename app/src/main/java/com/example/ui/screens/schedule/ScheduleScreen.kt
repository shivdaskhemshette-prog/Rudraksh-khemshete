package com.example.ui.screens.schedule

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudySlotEntity
import com.example.data.model.Subject
import com.example.ui.components.JeeTopHeader
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.JeeViewModel

@Composable
fun ScheduleScreen(
    viewModel: JeeViewModel,
    onNavigateToFocus: () -> Unit
) {
    val selectedDay by viewModel.selectedDay.collectAsState()
    val allSlots by viewModel.studySlots.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val currentDaySlots = remember(allSlots, selectedDay) {
        allSlots.filter { it.dayOfWeek == selectedDay }
    }

    val totalDaySlots = currentDaySlots.size
    val completedDaySlots = currentDaySlots.count { it.isCompleted }
    val progressFraction = if (totalDaySlots > 0) completedDaySlots.toFloat() / totalDaySlots else 0f

    var showAddDialog by remember { mutableStateOf(false) }
    var showRegenerateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            JeeTopHeader(
                title = "Study Planner",
                subtitle = "Target: ${profile?.targetExam ?: "JEE 2026"} • AIR < ${profile?.targetRank ?: 1000}",
                streakDays = profile?.currentStreak ?: 12,
                targetYear = profile?.targetYear ?: 2026,
                onFocusClick = onNavigateToFocus
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Slate950,
                modifier = Modifier.testTag("add_schedule_slot_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Slot")
            }
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
            // Day selector tabs
            item {
                DayOfWeekSelector(
                    selectedDay = selectedDay,
                    onSelectDay = { viewModel.selectDay(it) }
                )
            }

            // Progress Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(PhysicsCyan.copy(alpha = 0.5f), AccentIndigo.copy(alpha = 0.2f), Color.Transparent)
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
                                    text = "Daily Target Velocity",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "$completedDaySlots of $totalDaySlots modules conquered today",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate400
                                )
                            }
                            Text(
                                text = "${(progressFraction * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = PhysicsCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PhysicsCyan,
                            trackColor = Slate800
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { showRegenerateDialog = true },
                                modifier = Modifier.testTag("regenerate_schedule_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = PhysicsCyan
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI Schedule Generator",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PhysicsCyan
                                )
                            }

                            FilledTonalButton(
                                onClick = onNavigateToFocus,
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = PhysicsCyan.copy(alpha = 0.18f),
                                    contentColor = PhysicsCyan
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Deep Work", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Study slots list header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Study Modules",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${getDayName(selectedDay)} Plan",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (currentDaySlots.isEmpty()) {
                item {
                    EmptyScheduleState(onAddClick = { showAddDialog = true })
                }
            } else {
                items(currentDaySlots, key = { it.id }) { slot ->
                    StudySlotCard(
                        slot = slot,
                        onToggle = { isDone ->
                            viewModel.toggleSlotCompletion(slot.id, isDone)
                        }
                    )
                }
            }

            // Backlog & JEE Strategy Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Slate850
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AccentIndigo.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = AccentIndigo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "JEE 99th-Percentile Rule",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Revision is not optional. Give equal weightage to Physics mechanics, Organic reaction mechanisms, and Calculus. Complete daily slots before sleeping to safeguard your streak!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate400,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddStudySlotDialog(
            day = selectedDay,
            onDismiss = { showAddDialog = false },
            onConfirm = { time, subject, topic, duration ->
                viewModel.addNewStudySlot(selectedDay, time, subject, topic, duration)
                showAddDialog = false
            }
        )
    }

    if (showRegenerateDialog) {
        SmartScheduleDialog(
            currentGoalHours = profile?.dailyGoalHours ?: 8,
            onDismiss = { showRegenerateDialog = false },
            onGenerate = { hours ->
                viewModel.regeneratePersonalizedSchedule(hours)
                showRegenerateDialog = false
            }
        )
    }
}

@Composable
fun DayOfWeekSelector(
    selectedDay: Int,
    onSelectDay: (Int) -> Unit
) {
    val days = listOf(
        1 to "Mon", 2 to "Tue", 3 to "Wed",
        4 to "Thu", 5 to "Fri", 6 to "Sat", 7 to "Sun"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { (dayNum, label) ->
            val isSelected = dayNum == selectedDay
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectDay(dayNum) }
                    .testTag("day_picker_$dayNum"),
                color = if (isSelected) Slate850 else Slate900,
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = if (isSelected) PhysicsGradient else Brush.linearGradient(listOf(Slate800, Slate800))
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (isSelected) PhysicsCyan else Slate400
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) PhysicsCyan else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun StudySlotCard(
    slot: StudySlotEntity,
    onToggle: (Boolean) -> Unit
) {
    val subjectBorder = when (slot.subject.uppercase()) {
        "PHYSICS" -> PhysicsCyan
        "CHEMISTRY" -> ChemEmeraldLight
        "MATHEMATICS" -> MathAmberLight
        "REVISION" -> AccentIndigo
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("study_slot_${slot.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (slot.isCompleted) Slate900.copy(alpha = 0.5f) else Slate900
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (slot.isCompleted) Slate800 else Slate800.copy(alpha = 0.9f)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Subject color pill bar on left
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(38.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (slot.isCompleted) Slate700 else subjectBorder)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Checkbox(
                checked = slot.isCompleted,
                onCheckedChange = { onToggle(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Slate950
                ),
                modifier = Modifier.testTag("slot_checkbox_${slot.id}")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubjectBadge(subjectName = slot.subject)
                    Text(
                        text = slot.timeSlot,
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate400,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = slot.topic,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (slot.isCompleted) Slate600 else Color.White,
                    textDecoration = if (slot.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Slate850
            ) {
                Text(
                    text = "${slot.durationMinutes}m",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Slate400
                )
            }
        }
    }
}

@Composable
fun EmptyScheduleState(onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EventAvailable,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No study blocks scheduled",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap below to add topics or generate a personalized routine.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Add Study Block", color = Slate950)
            }
        }
    }
}

@Composable
fun AddStudySlotDialog(
    day: Int,
    onDismiss: () -> Unit,
    onConfirm: (time: String, subject: String, topic: String, duration: Int) -> Unit
) {
    var subject by remember { mutableStateOf("PHYSICS") }
    var topic by remember { mutableStateOf("") }
    var timeSlot by remember { mutableStateOf("07:00 AM - 09:30 AM") }
    var durationText by remember { mutableStateOf("150") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Study Slot for ${getDayName(day)}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Subject selection
                Text("Subject", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("PHYSICS", "CHEMISTRY", "MATHEMATICS", "REVISION").forEach { subj ->
                        FilterChip(
                            selected = subject == subj,
                            onClick = { subject = subj },
                            label = { Text(subj.take(4), fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Topic / Chapter (e.g. Thermodynamics PYQs)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_slot_topic_input")
                )

                OutlinedTextField(
                    value = timeSlot,
                    onValueChange = { timeSlot = it },
                    label = { Text("Time Slot") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it },
                    label = { Text("Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (topic.isNotBlank()) {
                        val dur = durationText.toIntOrNull() ?: 120
                        onConfirm(timeSlot, subject, topic, dur)
                    }
                },
                modifier = Modifier.testTag("confirm_add_slot_btn")
            ) {
                Text("Add Slot")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun SmartScheduleDialog(
    currentGoalHours: Int,
    onDismiss: () -> Unit,
    onGenerate: (hours: Int) -> Unit
) {
    var hours by remember { mutableStateOf(currentGoalHours.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = PhysicsCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Smart Schedule Generator")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Generate a balanced 7-day JEE schedule allocating equal weightage to Physics, Chemistry, Mathematics, and active mock revision based on your daily capacity.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Daily Study Target: ${hours.toInt()} Hours",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Slider(
                    value = hours,
                    onValueChange = { hours = it },
                    valueRange = 4f..14f,
                    steps = 9
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("4 hrs (Light)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("8 hrs (Standard)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("14 hrs (Intense)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onGenerate(hours.toInt()) },
                modifier = Modifier.testTag("generate_schedule_confirm_btn")
            ) {
                Text("Generate Plan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

fun getDayName(day: Int): String {
    return when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Today"
    }
}
