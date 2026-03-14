package com.davinci.app.data.repository

import com.davinci.app.data.local.dao.TaskDao
import com.davinci.app.data.local.entity.TaskEntity
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    // TODO: Add Supabase Postgrest client for remote sync
) : TaskRepository {

    override fun getTasks(category: TaskCategory?, status: TaskStatus?): Flow<List<Task>> {
        return if (category != null) {
            taskDao.getTasksByCategory(category.name.lowercase()).map { entities ->
                entities.map { it.toDomain() }
            }
        } else {
            taskDao.getAllTasks().map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }

    override fun getUrgentTasks(): Flow<List<Task>> {
        return taskDao.getUrgentTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createTask(
        title: String,
        category: TaskCategory,
        assignedTo: String?,
        dueDate: Instant?,
        isUrgent: Boolean,
        notes: String?,
        sharedWith: List<String>,
    ): Result<Task> {
        return try {
            val task = Task(
                id = UUID.randomUUID().toString(),
                title = title,
                category = category,
                assignedTo = assignedTo,
                assignedToName = if (assignedTo == null) "NKC" else null,
                createdBy = "current-user", // TODO: get from auth
                isUrgent = isUrgent,
                dueDate = dueDate,
                notes = notes,
                sharedWith = sharedWith,
            )
            taskDao.insertTask(TaskEntity.fromDomain(task))
            // TODO: Sync to Supabase
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Task> {
        return try {
            taskDao.updateTask(TaskEntity.fromDomain(task))
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleTaskStatus(taskId: String): Result<Task> {
        return try {
            val entity = taskDao.getTaskById(taskId) ?: return Result.failure(Exception("Task not found"))
            val newStatus = if (entity.status == "pending") "completed" else "pending"
            val updated = entity.copy(
                status = newStatus,
                completedAt = if (newStatus == "completed") System.currentTimeMillis() else null,
            )
            taskDao.updateTask(updated)
            Result.success(updated.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            taskDao.deleteTask(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
