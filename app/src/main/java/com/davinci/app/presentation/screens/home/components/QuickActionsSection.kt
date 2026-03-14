package com.davinci.app.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

@Composable
fun QuickActionsSection(
    onNavigateToTasks: () -> Unit,
    onNavigateToInvestment: () -> Unit,
    onNavigateToTimezone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard(
            icon = Icons.Outlined.Checklist,
            label = "Tasks",
            badgeCount = 3,
            bgColor = Color(0xFFE0F2F1),
            iconColor = DavinciColors.Primary,
            modifier = Modifier.weight(1f),
            onClick = onNavigateToTasks
        )
        QuickActionCard(
            icon = Icons.Outlined.AccountBalance,
            label = "Wealth",
            bgColor = Color(0xFFFFF8E1),
            iconColor = Color(0xFFF59E0B),
            modifier = Modifier.weight(1f),
            onClick = onNavigateToInvestment
        )
        QuickActionCard(
            icon = Icons.Outlined.Schedule,
            label = "Timezone",
            bgColor = Color(0xFFE8EAF6),
            iconColor = Color(0xFF5C6BC0),
            modifier = Modifier.weight(1f),
            onClick = onNavigateToTimezone
        )
        QuickActionCard(
            icon = Icons.Outlined.Settings,
            label = "Settings",
            bgColor = Color(0xFFF3E5F5),
            iconColor = Color(0xFF9C27B0),
            modifier = Modifier.weight(1f),
            onClick = { /* TODO: navigate to settings */ }
        )
    }
}

@Composable
fun QuickActionCard(
    icon: ImageVector,
    label: String,
    bgColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    badgeCount: Int? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = iconColor
            )
            if (badgeCount != null) {
                Box(
                    modifier = Modifier
                        .offset(x = 14.dp, y = (-4).dp)
                        .size(18.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(DavinciColors.Error),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$badgeCount",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = DavinciColors.TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}
