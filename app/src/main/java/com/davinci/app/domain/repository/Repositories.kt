package com.davinci.app.domain.repository

import com.davinci.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserProfile>
    suspend fun signUp(email: String, password: String, displayName: String): Result<UserProfile>
    suspend fun loginWithGoogle(): Result<UserProfile>
    suspend fun logout()
    suspend fun getCurrentUser(): UserProfile?
    fun isLoggedIn(): Flow<Boolean>
}

interface TaskRepository {
    fun getTasks(
        category: TaskCategory? = null,
        status: TaskStatus? = null
    ): Flow<List<Task>>
    fun getUrgentTasks(): Flow<List<Task>>
    suspend fun createTask(
        title: String,
        category: TaskCategory,
        assignedTo: String? = null,
        dueDate: java.time.Instant? = null,
        isUrgent: Boolean = false,
        notes: String? = null,
    ): Result<Task>
    suspend fun updateTask(task: Task): Result<Task>
    suspend fun toggleTaskStatus(taskId: String): Result<Task>
    suspend fun deleteTask(taskId: String): Result<Unit>
}

interface UserRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(profile: UserProfile): Result<UserProfile>
    suspend fun getFamilyMembers(): Result<List<FamilyMember>>
    suspend fun inviteFamilyMember(email: String): Result<Unit>
}

interface InvestmentRepository {
    fun getPrices(): Flow<List<InvestmentPrice>>
    suspend fun getHistory(metal: Metal, range: String): Result<List<PriceHistoryPoint>>
    suspend fun getNews(): Result<List<NewsItem>>
    suspend fun refreshPrices(): Result<Unit>
}
