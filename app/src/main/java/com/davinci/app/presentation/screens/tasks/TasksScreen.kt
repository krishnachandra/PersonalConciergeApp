package com.davinci.app.presentation.screens.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.domain.model.TaskStatus
import com.davinci.app.presentation.screens.tasks.components.CategoryTabs
import com.davinci.app.presentation.screens.tasks.components.CreateTaskSheet
import com.davinci.app.presentation.screens.tasks.components.TaskRow
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Tasks Screen — Shared family task management.
 *
 * Matches wireframe: "Shared Tasks" header, category tabs,
 * task list with checkboxes, completed section, FAB.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateSheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(DavinciColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ─── Header ──────────────────────────────────
            Text(
                text = "Shared Tasks",
                style = MaterialTheme.typography.displayMedium,
                color = DavinciColors.TextPrimary,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp),
            )

            // ─── Category Tabs ───────────────────────────
            CategoryTabs(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) },
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ─── Task List ───────────────────────────────
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp),
            ) {
                // Pending tasks
                val pendingTasks = uiState.tasks.filter { it.status == TaskStatus.PENDING }
                items(pendingTasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = { viewModel.toggleTask(task.id) },
                        modifier = Modifier.animateItem(),
                    )
                    HorizontalDivider(
                        color = DavinciColors.Divider,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }

                // Completed section
                val completedTasks = uiState.tasks.filter { it.status == TaskStatus.COMPLETED }
                if (completedTasks.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "COMPLETED",
                            style = MaterialTheme.typography.labelSmall,
                            color = DavinciColors.TextMuted,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            letterSpacing = androidx.compose.ui.unit.TextUnit(
                                1.5f,
                                androidx.compose.ui.unit.TextUnitType.Sp
                            ),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(completedTasks, key = { it.id }) { task ->
                        TaskRow(
                            task = task,
                            onToggle = { viewModel.toggleTask(task.id) },
                            isCompleted = true,
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }

        // ─── FAB ─────────────────────────────────────────
        FloatingActionButton(
            onClick = { showCreateSheet = true },
            containerColor = DavinciColors.Primary,
            contentColor = DavinciColors.TextOnPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(56.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add task",
                modifier = Modifier.size(24.dp),
            )
        }
    }

    // ─── Create Task Bottom Sheet ────────────────────────
    if (showCreateSheet) {
        CreateTaskSheet(
            onDismiss = { showCreateSheet = false },
            onCreateTask = { title, category, assignee, isUrgent ->
                viewModel.createTask(title, category, assignee, isUrgent)
                showCreateSheet = false
            },
        )
    }
}
