package com.davinci.app.data.local.dao

import androidx.room.*
import com.davinci.app.data.local.entity.TaskEntity
import com.davinci.app.data.local.entity.PriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE status != 'deleted' ORDER BY isUrgent DESC, createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE category = :category AND status != 'deleted' ORDER BY isUrgent DESC, createdAt DESC")
    fun getTasksByCategory(category: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isUrgent = 1 AND status = 'pending' ORDER BY dueDate ASC")
    fun getUrgentTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}

@Dao
interface PriceDao {
    @Query("SELECT * FROM price_cache")
    fun getAllPrices(): Flow<List<PriceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrice(price: PriceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<PriceEntity>)

    @Query("DELETE FROM price_cache")
    suspend fun clearAll()
}
