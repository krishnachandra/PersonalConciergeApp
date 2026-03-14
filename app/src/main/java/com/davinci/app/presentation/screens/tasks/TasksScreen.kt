package com.davinci.app.presentation.screens.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 24.dp, end = 12.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shared Tasks",
                    style = MaterialTheme.typography.displayMedium,
                    color = DavinciColors.TextPrimary,
                )
                
                Box {
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = DavinciColors.TextPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(DavinciColors.Surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Refresh", color = DavinciColors.TextPrimary) },
                            onClick = { menuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Mark All Done", color = DavinciColors.TextPrimary) },
                            onClick = { menuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Clear Completed", color = DavinciColors.TextPrimary) },
                            onClick = { menuExpanded = false }
                        )
                    }
                }
            }

            // ─── Category Tabs ───────────────────────────
            CategoryTabs(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) },
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Sort Section ────────────────────────────
            SortSection(
                currentSortType = uiState.sortType,
                currentSortOrder = uiState.sortOrder,
                onSortTypeChanged = { viewModel.updateSort(it) },
                onSortOrderChanged = { viewModel.updateSortOrder(it) },
                modifier = Modifier.padding(horizontal = 24.dp)
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
                        showCategory = false,
                        modifier = Modifier.animateItem(),
                    )
                    HorizontalDivider(
                        color = DavinciColors.Divider,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }

                // Completed section header
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "COMPLETED",
                        style = MaterialTheme.typography.labelSmall,
                        color = DavinciColors.TextMuted,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        letterSpacing = androidx.compose.ui.unit.TextUnit(
                            1.5f,
                            androidx.compose.ui.unit.TextUnitType.Sp
                        ),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = DavinciColors.Divider,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                val completedTasks = uiState.tasks.filter { it.status == TaskStatus.COMPLETED }
                if (completedTasks.isEmpty()) {
                    item {
                        Text(
                            text = "No completed tasks yet",
                            style = MaterialTheme.typography.bodySmall,
                            color = DavinciColors.TextMuted,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                            fontStyle = FontStyle.Italic
                        )
                    }
                } else {
                    items(completedTasks, key = { it.id }) { task ->
                        TaskRow(
                            task = task,
                            onToggle = { viewModel.toggleTask(task.id) },
                            isCompleted = true,
                            showCategory = false,
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
@Composable
fun SortSection(
    currentSortType: SortType,
    currentSortOrder: SortOrder,
    onSortTypeChanged: (SortType) -> Unit,
    onSortOrderChanged: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = null,
            tint = DavinciColors.TextMuted,
            modifier = Modifier.size(18.dp)
        )
        
        Text(
            text = "Sort by:",
            style = MaterialTheme.typography.labelMedium,
            color = DavinciColors.TextMuted
        )

        FilterChip(
            selected = currentSortType == SortType.CREATION_DATE,
            onClick = { onSortTypeChanged(SortType.CREATION_DATE) },
            label = { Text("Date") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DavinciColors.Primary.copy(alpha = 0.1f),
                selectedLabelColor = DavinciColors.Primary
            ),
            border = null
        )

        FilterChip(
            selected = currentSortType == SortType.ETA,
            onClick = { onSortTypeChanged(SortType.ETA) },
            label = { Text("ETA") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DavinciColors.Primary.copy(alpha = 0.1f),
                selectedLabelColor = DavinciColors.Primary
            ),
            border = null
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {
                val nextOrder = if (currentSortOrder == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
                onSortOrderChanged(nextOrder)
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (currentSortOrder == SortOrder.ASCENDING) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = "Toggle Sort Order",
                tint = DavinciColors.Primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
