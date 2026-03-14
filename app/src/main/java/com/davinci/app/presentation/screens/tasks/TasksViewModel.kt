package com.davinci.app.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortType { CREATION_DATE, ETA }
enum class SortOrder { ASCENDING, DESCENDING }

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val categories: List<String> = listOf("All", "Personal", "Purchases", "Finances"),
    val selectedCategory: String = "All",
    val sortType: SortType = SortType.CREATION_DATE,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val isLoading: Boolean = true,
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState(
        categories = listOf("All", "Personal", "Purchases", "Finances")
    ))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val selectedCategory = MutableStateFlow("All")
    private val sortType = MutableStateFlow(SortType.CREATION_DATE)
    private val sortOrder = MutableStateFlow(SortOrder.DESCENDING)

    init {
        viewModelScope.launch {
            combine(selectedCategory, sortType, sortOrder) { category, type, order ->
                Triple(category, type, order)
            }.flatMapLatest { (category, type, order) ->
                val taskCategory = when (category) {
                    "Personal" -> TaskCategory.PERSONAL
                    "Purchases" -> TaskCategory.PURCHASES
                    "Finances" -> TaskCategory.FINANCES
                    else -> null
                }
                
                taskRepository.getTasks(category = taskCategory).map { tasks ->
                    val filteredTasks = tasks // Categories filtered via Repository
                    sortTasks(filteredTasks, type, order)
                }
            }.collect { sortedTasks ->
                _uiState.update {
                    it.copy(tasks = sortedTasks, isLoading = false)
                }
            }
        }
    }

    private fun sortTasks(tasks: List<Task>, type: SortType, order: SortOrder): List<Task> {
        return when (type) {
            SortType.CREATION_DATE -> {
                if (order == SortOrder.ASCENDING) tasks.sortedBy { it.createdAt }
                else tasks.sortedByDescending { it.createdAt }
            }
            SortType.ETA -> {
                if (order == SortOrder.ASCENDING) tasks.sortedBy { it.dueDate ?: java.time.Instant.MAX }
                else tasks.sortedByDescending { it.dueDate ?: java.time.Instant.MIN }
            }
        }
    }

    fun updateSort(type: SortType) {
        sortType.value = type
        _uiState.update { it.copy(sortType = type) }
    }

    fun updateSortOrder(order: SortOrder) {
        sortOrder.value = order
        _uiState.update { it.copy(sortOrder = order) }
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
        dueDate: java.time.Instant? = null,
        sharedWith: List<String> = emptyList(),
    ) {
        viewModelScope.launch {
            taskRepository.createTask(
                title = title,
                category = category,
                assignedTo = assignee,
                isUrgent = isUrgent,
                dueDate = dueDate,
                sharedWith = sharedWith,
            )
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }
}
