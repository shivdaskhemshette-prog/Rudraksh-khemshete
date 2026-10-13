package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun JeeTopHeader(
    title: String,
    subtitle: String? = null,
    streakDays: Int = 12,
    targetYear: Int = 2026,
    onFocusClick: () -> Unit
) {
    // Subtle breathing pulse for focus button
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = PhysicsCyan.copy(alpha = 0.2f)),
        colors = CardDefaults.cardColors(
            containerColor = Slate900
        ),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(PhysicsCyan.copy(alpha = 0.4f), AccentIndigo.copy(alpha = 0.2f), Color.Transparent)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(PhysicsCyan)
                        )
                    }

                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Streak Badge with fiery glow
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = WarningGold.copy(alpha = 0.15f),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(WarningGold, MathSunset)
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = WarningGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${streakDays}d Streak",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = WarningGold
                            )
                        }
                    }

                    // Focus Timer Quick Button with glowing indicator
                    FilledTonalIconButton(
                        onClick = onFocusClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("header_focus_button"),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = PhysicsCyan.copy(alpha = 0.18f),
                            contentColor = PhysicsCyan
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Focus Timer",
                            modifier = Modifier.size(20.dp),
                            tint = PhysicsCyan.copy(alpha = pulseAlpha)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-status line: Exam countdown & Target badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate850
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "JEE Main 2026: 94 Days Remaining",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate200
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentIndigo.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Target: AIR < 1000",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA5B4FC),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun JeeBottomNavigation(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Slate950,
        tonalElevation = 12.dp
    ) {
        val items = listOf(
            Triple(AppDestination.SCHEDULE, Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
            Triple(AppDestination.MOCK_TESTS, Icons.Default.Quiz, Icons.Outlined.Quiz),
            Triple(AppDestination.QUESTION_BANK, Icons.Default.Help, Icons.Outlined.HelpOutline),
            Triple(AppDestination.FORMULAS, Icons.Default.MenuBook, Icons.Outlined.MenuBook),
            Triple(AppDestination.ANALYTICS, Icons.Default.Leaderboard, Icons.Outlined.Leaderboard),
            Triple(AppDestination.FOCUS_MODE, Icons.Default.SelfImprovement, Icons.Outlined.SelfImprovement)
        )

        items.forEach { (destination, selectedIcon, unselectedIcon) ->
            val isSelected = currentDestination == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) selectedIcon else unselectedIcon,
                        contentDescription = destination.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PhysicsCyan,
                    selectedTextColor = PhysicsCyan,
                    indicatorColor = PhysicsCyan.copy(alpha = 0.15f),
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_tab_${destination.name.lowercase()}")
            )
        }
    }
}

@Composable
fun JeeDesktopSidebarRail(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    streakDays: Int = 12
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = Slate950,
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate850,
                    border = CardDefaults.outlinedCardBorder().copy(brush = PhysicsGradient),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "JEE Ace",
                            tint = PhysicsCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "JEE Ace",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    ) {
        val items = listOf(
            Triple(AppDestination.SCHEDULE, Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
            Triple(AppDestination.MOCK_TESTS, Icons.Default.Quiz, Icons.Outlined.Quiz),
            Triple(AppDestination.QUESTION_BANK, Icons.Default.Help, Icons.Outlined.HelpOutline),
            Triple(AppDestination.FORMULAS, Icons.Default.MenuBook, Icons.Outlined.MenuBook),
            Triple(AppDestination.ANALYTICS, Icons.Default.Leaderboard, Icons.Outlined.Leaderboard),
            Triple(AppDestination.FOCUS_MODE, Icons.Default.SelfImprovement, Icons.Outlined.SelfImprovement)
        )

        items.forEach { (destination, selectedIcon, unselectedIcon) ->
            val isSelected = currentDestination == destination
            NavigationRailItem(
                selected = isSelected,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) selectedIcon else unselectedIcon,
                        contentDescription = destination.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = PhysicsCyan,
                    selectedTextColor = PhysicsCyan,
                    indicatorColor = PhysicsCyan.copy(alpha = 0.15f),
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("rail_tab_${destination.name.lowercase()}")
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = WarningGold.copy(alpha = 0.15f),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = WarningGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${streakDays}d",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarningGold
                )
            }
        }
    }
}

@Composable
fun SubjectBadge(
    subjectName: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderBrush, label) = when (subjectName.uppercase()) {
        "PHYSICS" -> Quad(
            PhysicsCyan.copy(alpha = 0.12f),
            PhysicsCyan,
            PhysicsGradient,
            "Physics"
        )
        "CHEMISTRY" -> Quad(
            ChemEmerald.copy(alpha = 0.12f),
            ChemEmeraldLight,
            ChemGradient,
            "Chemistry"
        )
        "MATHEMATICS" -> Quad(
            MathAmber.copy(alpha = 0.12f),
            MathAmberLight,
            MathGradient,
            "Mathematics"
        )
        "REVISION" -> Quad(
            AccentIndigo.copy(alpha = 0.15f),
            Color(0xFFA5B4FC),
            Brush.horizontalGradient(listOf(AccentIndigo, AccentPurple)),
            "Revision"
        )
        "MOCK_TEST" -> Quad(
            ErrorRose.copy(alpha = 0.15f),
            Color(0xFFFDA4AF),
            Brush.horizontalGradient(listOf(ErrorRose, MathSunset)),
            "Mock Test"
        )
        else -> Quad(
            Slate800,
            Slate200,
            Brush.horizontalGradient(listOf(Slate700, Slate600)),
            subjectName
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = CardDefaults.outlinedCardBorder().copy(brush = borderBrush)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = 10.sp
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
