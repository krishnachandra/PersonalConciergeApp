package com.davinci.app.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

@Composable
fun UrgentTasksSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        UrgentTaskRow(
            title = "Approve Q4 Budget Transfer",
            subtitle = "Due in 2 hrs • Finances",
            subtitleColor = DavinciColors.Error,
            avatarInitials = "NKC"
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        UrgentTaskRow(
            title = "Call Contractor regarding roof",
            subtitle = "Purchases",
            subtitleColor = DavinciColors.TextMuted,
            avatarInitials = "ME"
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        UrgentTaskRow(
            title = "Review Mom's Visa Application",
            subtitle = "Personal",
            subtitleColor = DavinciColors.TextMuted,
            avatarInitials = "NKC"
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
    }
}

@Composable
fun UrgentTaskRow(
    title: String,
    subtitle: String,
    subtitleColor: Color,
    avatarInitials: String? = null,
    avatarImage: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox outline (Custom scaled out box)
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(1.5.dp, DavinciColors.Divider, RoundedCornerShape(2.dp))
                .background(Color.Transparent)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = subtitleColor
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Avatar Graphic
        if (avatarInitials != null) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCCFBF1)) // Light cyan/teal
                    .border(1.dp, Color(0xFF6EE7B7), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarInitials,
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        } else if (avatarImage) {
             Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F766E)), // Placeholder for a real photo, darker teal
                contentAlignment = Alignment.Center
            ) {
                // Simulating an image
            }
        }
    }
}
