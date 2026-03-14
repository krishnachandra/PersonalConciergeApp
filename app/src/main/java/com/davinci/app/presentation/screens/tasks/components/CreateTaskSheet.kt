package com.davinci.app.presentation.screens.tasks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davinci.app.domain.model.TaskCategory
import com.davinci.app.presentation.components.AvatarChip
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Bottom sheet for creating a new task.
 * Minimalist overlay per the design spec.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskSheet(
    onDismiss: () -> Unit,
    onCreateTask: (String, TaskCategory, String?, Boolean) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.PERSONAL) }
    var isUrgent by remember { mutableStateOf(false) }
    var selectedUsers by remember { mutableStateOf(setOf<String>()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DavinciColors.Background,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            Text(
                text = "New Task",
                style = MaterialTheme.typography.headlineMedium,
                color = DavinciColors.TextPrimary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("What needs to be done?", color = DavinciColors.TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DavinciColors.Surface,
                    unfocusedContainerColor = DavinciColors.Surface,
                    focusedBorderColor = DavinciColors.Primary,
                    unfocusedBorderColor = DavinciColors.Surface,
                    cursorColor = DavinciColors.Primary,
                ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category chips
            Text(
                text = "CATEGORY",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TaskCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.label, style = MaterialTheme.typography.labelMedium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DavinciColors.PrimaryLight,
                            selectedLabelColor = DavinciColors.Primary,
                            containerColor = DavinciColors.Surface,
                            labelColor = DavinciColors.TextMuted,
                        ),
                        border = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Urgent toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Text(
                    text = "Mark as urgent",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DavinciColors.TextPrimary,
                )
                Switch(
                    checked = isUrgent,
                    onCheckedChange = { isUrgent = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DavinciColors.TextOnPrimary,
                        checkedTrackColor = DavinciColors.Primary,
                        uncheckedThumbColor = DavinciColors.TextMuted,
                        uncheckedTrackColor = DavinciColors.SurfaceVariant,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Share section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (selectedUsers.isEmpty()) "Share with" else "Share with (${selectedUsers.size})",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DavinciColors.TextPrimary,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val users = listOf("NPS", "JPL")
                    users.forEach { userId ->
                        AvatarChip(
                            initials = userId,
                            size = 32.dp,
                            isSelected = selectedUsers.contains(userId),
                            modifier = Modifier.clickable {
                                selectedUsers = if (selectedUsers.contains(userId)) {
                                    selectedUsers - userId
                                } else {
                                    selectedUsers + userId
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Create button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreateTask(title, selectedCategory, null, isUrgent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DavinciColors.Primary,
                    contentColor = DavinciColors.TextOnPrimary,
                ),
                enabled = title.isNotBlank(),
            ) {
                Text("Create Task", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
