package com.davinci.app.presentation.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

@Composable
fun CompletedTasksSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CompletedTaskRow(
            title = "Book Flight to Seattle",
            subtitle = "Travel • 12-Mar-2026",
            initials = "NK"
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        CompletedTaskRow(
            title = "Monthly Rent Payment",
            subtitle = "Finance • 10-Mar-2026",
            initials = "NK"
        )
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
    }
}

@Composable
fun CompletedTaskRow(
    title: String,
    subtitle: String,
    initials: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Completed",
            tint = DavinciColors.Completed,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = DavinciColors.Completed,
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = DavinciColors.Completed
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Minimal initials circle for completed tasks
        Surface(
            modifier = Modifier.size(26.dp),
            shape = CircleShape,
            color = DavinciColors.Completed.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.Completed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
