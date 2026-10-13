package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.JeeBottomNavigation
import com.example.ui.screens.analytics.AnalyticsScreen
import com.example.ui.screens.focus.FocusModeScreen
import com.example.ui.screens.formulas.FormulaHandbookScreen
import com.example.ui.screens.mocktest.MockTestsListScreen
import com.example.ui.screens.mocktest.TestExamScreen
import com.example.ui.screens.mocktest.TestResultScreen
import com.example.ui.screens.questions.QuestionBankScreen
import com.example.ui.screens.schedule.ScheduleScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.JeeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val jeeViewModel: JeeViewModel = viewModel()
                    JeeMainApp(viewModel = jeeViewModel)
                }
            }
        }
    }
}

@Composable
fun JeeMainApp(viewModel: JeeViewModel) {
    val currentDestination by viewModel.currentScreen.collectAsState()
    val activeTestSession by viewModel.activeTestSession.collectAsState()
    val profile by viewModel.profile.collectAsState()

    // Handle back button for sub-screens
    BackHandler(enabled = activeTestSession == null && currentDestination != AppDestination.SCHEDULE) {
        viewModel.navigateTo(AppDestination.SCHEDULE)
    }

    val session = activeTestSession
    if (session != null) {
        if (session.isSubmitted) {
            TestResultScreen(
                session = session,
                onDone = {
                    viewModel.exitTestSession()
                    viewModel.navigateTo(AppDestination.MOCK_TESTS)
                }
            )
        } else {
            TestExamScreen(
                viewModel = viewModel,
                session = session,
                onBack = {
                    viewModel.exitTestSession()
                }
            )
        }
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 720.dp

            if (isWideScreen) {
                // Mac / Desktop / Tablet Layout: Sidebar Navigation Rail + Wide Content
                Row(modifier = Modifier.fillMaxSize()) {
                    if (currentDestination != AppDestination.FOCUS_MODE) {
                        com.example.ui.components.JeeDesktopSidebarRail(
                            currentDestination = currentDestination,
                            onNavigate = { destination -> viewModel.navigateTo(destination) },
                            streakDays = profile?.currentStreak ?: 12
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = androidx.compose.ui.Alignment.TopCenter
                    ) {
                        Box(modifier = Modifier.widthIn(max = 1100.dp).fillMaxSize()) {
                            AppScreenContent(
                                currentDestination = currentDestination,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            } else {
                // Mobile Handheld Layout: Bottom Navigation Bar
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentDestination != AppDestination.FOCUS_MODE) {
                            JeeBottomNavigation(
                                currentDestination = currentDestination,
                                onNavigate = { destination ->
                                    viewModel.navigateTo(destination)
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AppScreenContent(
                            currentDestination = currentDestination,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppScreenContent(
    currentDestination: AppDestination,
    viewModel: JeeViewModel
) {
    when (currentDestination) {
        AppDestination.SCHEDULE -> {
            ScheduleScreen(
                viewModel = viewModel,
                onNavigateToFocus = { viewModel.navigateTo(AppDestination.FOCUS_MODE) }
            )
        }
        AppDestination.MOCK_TESTS -> {
            MockTestsListScreen(
                viewModel = viewModel,
                onStartTest = { title, duration, questions ->
                    viewModel.startTest(title, duration, questions)
                },
                onNavigateToFocus = { viewModel.navigateTo(AppDestination.FOCUS_MODE) }
            )
        }
        AppDestination.QUESTION_BANK -> {
            QuestionBankScreen(
                viewModel = viewModel,
                onNavigateToFocus = { viewModel.navigateTo(AppDestination.FOCUS_MODE) }
            )
        }
        AppDestination.FORMULAS -> {
            FormulaHandbookScreen(
                viewModel = viewModel,
                onNavigateToFocus = { viewModel.navigateTo(AppDestination.FOCUS_MODE) }
            )
        }
        AppDestination.ANALYTICS -> {
            AnalyticsScreen(
                viewModel = viewModel,
                onNavigateToFocus = { viewModel.navigateTo(AppDestination.FOCUS_MODE) }
            )
        }
        AppDestination.FOCUS_MODE -> {
            FocusModeScreen(
                viewModel = viewModel,
                onBack = { viewModel.navigateTo(AppDestination.SCHEDULE) }
            )
        }
    }
}
