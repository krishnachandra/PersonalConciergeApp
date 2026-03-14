package com.davinci.app.data.repository

import com.davinci.app.data.local.dao.TaskDao
import com.davinci.app.data.local.entity.TaskEntity
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.TaskRepository
import com.davinci.app.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class SupabaseTaskDto(
    val id: String,
    val title: String,
    val notes: String? = null,
    val category: String,
    val assignedTo: String? = null,
    val assignedToName: String? = null,
    val assignedToAvatar: String? = null,
    val createdBy: String,
    val isUrgent: Boolean = false,
    val status: String = "pending",
    val dueDate: Long? = null,
    val completedAt: Long? = null,
    val createdAt: Long,
    val sharedWith: String? = null,
) {
    companion object {
        fun fromDomain(task: Task) = SupabaseTaskDto(
            id = task.id,
            title = task.title,
            notes = task.notes,
            category = task.category.name.lowercase(),
            assignedTo = task.assignedTo,
            assignedToName = task.assignedToName,
            assignedToAvatar = task.assignedToAvatar,
            createdBy = task.createdBy,
            isUrgent = task.isUrgent,
            status = task.status.name.lowercase(),
            dueDate = task.dueDate?.toEpochMilli(),
            completedAt = task.completedAt?.toEpochMilli(),
            createdAt = task.createdAt.toEpochMilli(),
            sharedWith = if (task.sharedWith.isEmpty()) null else task.sharedWith.joinToString(","),
        )
    }
}

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val supabaseProvider: SupabaseClientProvider,
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
            
            // Sync to Supabase
            try {
                supabaseProvider.client.postgrest["tasks"].insert(SupabaseTaskDto.fromDomain(task))
            } catch (e: Exception) {
                e.printStackTrace()
                // Proceed with local success even if remote fails
            }
            
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Task> {
        return try {
            taskDao.updateTask(TaskEntity.fromDomain(task))

            // Sync to Supabase
            try {
                supabaseProvider.client.postgrest["tasks"].update(
                    {
                        set("title", task.title)
                        set("notes", task.notes)
                        set("category", task.category.name.lowercase())
                        set("assignedTo", task.assignedTo)
                        set("assignedToName", task.assignedToName)
                        set("assignedToAvatar", task.assignedToAvatar)
                        set("isUrgent", task.isUrgent)
                        set("status", task.status.name.lowercase())
                        set("dueDate", task.dueDate?.toEpochMilli())
                        set("completedAt", task.completedAt?.toEpochMilli())
                        set("sharedWith", if (task.sharedWith.isEmpty()) null else task.sharedWith.joinToString(","))
                    }
                ) {
                    filter { eq("id", task.id) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

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

            // Sync to Supabase
            try {
                supabaseProvider.client.postgrest["tasks"].update(
                    {
                        set("status", updated.status)
                        set("completedAt", updated.completedAt)
                    }
                ) {
                    filter { eq("id", taskId) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Result.success(updated.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            taskDao.deleteTask(taskId)

            // Sync to Supabase
            try {
                supabaseProvider.client.postgrest["tasks"].delete {
                    filter { eq("id", taskId) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
