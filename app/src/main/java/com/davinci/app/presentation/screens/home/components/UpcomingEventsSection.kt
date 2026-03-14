package com.davinci.app.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.*
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
fun UpcomingEventsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        EventRow(
            title = "Dentist Appointment",
            time = "4:30 PM Today",
            icon = Icons.Outlined.Event,
            iconBg = Color(0xFFE3F2FD),
            iconColor = Color(0xFF1976D2)
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        EventRow(
            title = "Family Dinner - Taj Falaknuma",
            time = "8:00 PM Tomorrow",
            icon = Icons.Outlined.Event,
            iconBg = Color(0xFFFFF3E0),
            iconColor = Color(0xFFE65100)
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        EventRow(
            title = "Tax Filing Deadline",
            time = "Mar 31, 2026",
            icon = Icons.Outlined.CalendarMonth,
            iconBg = Color(0xFFFCE4EC),
            iconColor = Color(0xFFC62828)
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
    }
}

@Composable
fun EventRow(
    title: String,
    time: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(20.dp),
                tint = iconColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = DavinciColors.TextMuted
            )
        }
    }
}
