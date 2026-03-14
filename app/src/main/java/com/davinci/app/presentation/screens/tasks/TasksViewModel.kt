package com.davinci.app.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val categories: List<String> = listOf("All", "Mine", "Admin", "Groceries", "Finance"),
    val selectedCategory: String = "All",
    val isLoading: Boolean = true,
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val selectedCategory = MutableStateFlow("All")

    init {
        viewModelScope.launch {
            selectedCategory.flatMapLatest { category ->
                val taskCategory = when (category) {
                    "Admin" -> TaskCategory.ADMIN
                    "Groceries" -> TaskCategory.GROCERIES
                    "Finance" -> TaskCategory.FINANCE
                    else -> null
                }
                taskRepository.getTasks(category = taskCategory)
            }.collect { tasks ->
                _uiState.update {
                    it.copy(tasks = tasks, isLoading = false)
                }
            }
        }
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.toggleTaskStatus(taskId)
        }
    }

    fun createTask(
        title: String,
        category: TaskCategory,
        assignee: String?,
        isUrgent: Boolean,
    ) {
        viewModelScope.launch {
            taskRepository.createTask(
                title = title,
                category = category,
                assignedTo = assignee,
                isUrgent = isUrgent,
            )
        }
    }
}
