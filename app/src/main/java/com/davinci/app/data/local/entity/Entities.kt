package com.davinci.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.davinci.app.domain.model.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
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
    val createdAt: Long = System.currentTimeMillis(),
    val sharedWith: String? = null,
) {
    fun toDomain() = Task(
        id = id,
        title = title,
        notes = notes,
        category = TaskCategory.entries.find { it.name == category.uppercase() } ?: TaskCategory.PERSONAL,
        assignedTo = assignedTo,
        assignedToName = assignedToName,
        assignedToAvatar = assignedToAvatar,
        createdBy = createdBy,
        isUrgent = isUrgent,
        status = if (status == "completed") TaskStatus.COMPLETED else TaskStatus.PENDING,
        dueDate = dueDate?.let { java.time.Instant.ofEpochMilli(it) },
        completedAt = completedAt?.let { java.time.Instant.ofEpochMilli(it) },
        createdAt = java.time.Instant.ofEpochMilli(createdAt),
        sharedWith = sharedWith?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
    )

    companion object {
        fun fromDomain(task: Task) = TaskEntity(
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

@Entity(tableName = "price_cache")
data class PriceEntity(
    @PrimaryKey val metal: String,
    val priceUsd: Double,
    val change24h: Double,
    val direction: String,
    val fetchedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain() = InvestmentPrice(
        metal = Metal.entries.find { it.name == metal.uppercase() } ?: Metal.GOLD,
        priceUsd = priceUsd,
        change24h = change24h,
        direction = if (direction == "up") PriceDirection.UP else PriceDirection.DOWN,
        lastUpdated = java.time.Instant.ofEpochMilli(fetchedAt),
    )
}
