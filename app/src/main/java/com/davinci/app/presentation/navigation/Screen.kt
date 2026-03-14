package com.davinci.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation destinations for the Davinci app.
 */
sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    data object Home : Screen(
        route = "home",
        label = "Briefing",
        icon = Icons.Outlined.Dashboard,
        selectedIcon = Icons.Filled.Dashboard,
    )
    data object Tasks : Screen(
        route = "tasks",
        label = "Tasks",
        icon = Icons.Outlined.Checklist,
        selectedIcon = Icons.Filled.Checklist,
    )
    data object Investment : Screen(
        route = "investment",
        label = "Wealth",
        icon = Icons.Outlined.BarChart,
        selectedIcon = Icons.Filled.BarChart,
    )
    data object Settings : Screen(
        route = "settings",
        label = "Settings",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
    )
    data object Login : Screen(
        route = "login",
        label = "Login",
        icon = Icons.Outlined.Dashboard,
        selectedIcon = Icons.Filled.Dashboard,
    )
    data object SignUp : Screen(
        route = "signup",
        label = "Sign Up",
        icon = Icons.Outlined.Dashboard,
        selectedIcon = Icons.Filled.Dashboard,
    )

    companion object {
        val bottomNavItems = listOf(Home, Tasks, Investment, Settings)
    }
}
