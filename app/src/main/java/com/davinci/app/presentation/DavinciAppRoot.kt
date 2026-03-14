package com.davinci.app.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.davinci.app.presentation.navigation.BottomNavBar
import com.davinci.app.presentation.navigation.DavinciNavHost
import com.davinci.app.presentation.navigation.Screen

/**
 * Root composable for the Davinci app. Handles top-level scaffold
 * with conditional bottom navigation bar.
 */
@Composable
fun DavinciAppRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom nav on auth screens
    val showBottomNav = currentRoute in Screen.bottomNavItems.map { it.route }

    // TODO: Check auth state to determine start destination
    val startDestination = Screen.Login.route

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        DavinciNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
