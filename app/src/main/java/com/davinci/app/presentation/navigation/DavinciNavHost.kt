package com.davinci.app.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davinci.app.presentation.screens.auth.LoginScreen
import com.davinci.app.presentation.screens.auth.SignUpScreen
import com.davinci.app.presentation.screens.home.HomeScreen
import com.davinci.app.presentation.screens.investment.InvestmentScreen
import com.davinci.app.presentation.screens.settings.SettingsScreen
import com.davinci.app.presentation.screens.tasks.TasksScreen
import com.davinci.app.presentation.screens.timezone.TimezoneScreen

@Composable
fun DavinciNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
    ) {
        // ─── Auth ────────────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        // ─── Main Tabs ──────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTasks = {
                    navController.navigate(Screen.Tasks.route)
                },
                onNavigateToInvestment = {
                    navController.navigate(Screen.Investment.route)
                },
                onNavigateToTimezone = {
                    navController.navigate(Screen.Timezone.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Tasks.route) {
            TasksScreen()
        }

        composable(Screen.Investment.route) {
            InvestmentScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.Timezone.route) {
            TimezoneScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
