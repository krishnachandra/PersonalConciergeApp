package com.davinci.app.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = DavinciColors.Surface,
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(
                color = DavinciColors.Divider,
                thickness = 0.5.dp
            )
            NavigationBar(
                modifier = modifier.fillMaxWidth(),
                containerColor = DavinciColors.Surface,
                tonalElevation = 0.dp, // Elevation handled by Surface
            ) {
                Screen.bottomNavItems.forEach { screen ->
                    val isSelected = currentRoute == screen.route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavigate(screen) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.icon,
                                contentDescription = screen.label,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = {
                            Text(
                                text = screen.label,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DavinciColors.Primary,
                            selectedTextColor = DavinciColors.Primary,
                            unselectedIconColor = DavinciColors.TextMuted,
                            unselectedTextColor = DavinciColors.TextMuted,
                            indicatorColor = DavinciColors.PrimaryLight,
                        ),
                    )
                }
            }
        }
    }
}
