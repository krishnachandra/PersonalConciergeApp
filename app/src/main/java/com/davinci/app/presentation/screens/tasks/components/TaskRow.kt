package com.davinci.app.presentation.screens.tasks.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davinci.app.domain.model.Task
import com.davinci.app.presentation.components.AvatarChip
import com.davinci.app.presentation.theme.DavinciColors
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Single task row — checkbox, title, category, urgent badge, avatar.
 * Matches wireframe task list item layout.
 */
@Composable
fun TaskRow(
    task: Task,
    onToggle: () -> Unit,
    isCompleted: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember { 
        DateTimeFormatter.ofPattern("dd-MMM-yyyy").withZone(ZoneId.systemDefault()) 
    }
    val createdDate = dateFormatter.format(task.createdAt)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Checkbox
        if (isCompleted) {
            Checkbox(
                checked = true,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = DavinciColors.CompletedCheck,
                    checkmarkColor = DavinciColors.TextOnPrimary,
                ),
                modifier = Modifier.size(24.dp),
            )
        } else {
            Checkbox(
                checked = false,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = DavinciColors.Divider,
                ),
                modifier = Modifier.size(24.dp),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Task title + category
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isCompleted) DavinciColors.Completed else {
                    if (task.isUrgent) DavinciColors.Negative else DavinciColors.TextPrimary
                },
                fontWeight = if (task.isUrgent && !isCompleted) FontWeight.Medium else FontWeight.Normal,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (task.isUrgent && !isCompleted) {
                    Text(
                        text = "⚠ ",
                        style = MaterialTheme.typography.bodySmall,
                        color = DavinciColors.Warning,
                    )
                }
                Text(
                    text = buildString {
                        append(task.category.label)
                        append(" • $createdDate")
                        if (task.isUrgent && !isCompleted) append(" • Urgent")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCompleted) DavinciColors.Completed else DavinciColors.TextMuted,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = DavinciColors.TextMuted
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Shared with: NPS & JPL",
                    style = MaterialTheme.typography.bodySmall,
                    color = DavinciColors.TextMuted
                )
            }
        }

        // Avatar
        AvatarChip(
            imageUrl = task.assignedToAvatar,
            initials = task.assignedToName?.take(3) ?: "NKC",
            size = 32.dp,
        )
    }
}
