package com.davinci.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Domain model for a family task.
 */
data class Task(
    val id: String,
    val title: String,
    val notes: String? = null,
    val category: TaskCategory,
    val assignedTo: String? = null,
    val assignedToName: String? = null,
    val assignedToAvatar: String? = null,
    val createdBy: String,
    val isUrgent: Boolean = false,
    val status: TaskStatus = TaskStatus.PENDING,
    val dueDate: Instant? = null,
    val completedAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
)

enum class TaskCategory(val label: String) {
    ADMIN("Admin"),
    GROCERIES("Groceries"),
    FINANCE("Finance"),
    TRAVEL("Travel"),
    HOME("Home"),
    FAMILY("Family"),
}

enum class TaskStatus {
    PENDING, COMPLETED
}

/**
 * Domain model for investment price data.
 */
data class InvestmentPrice(
    val metal: Metal,
    val priceUsd: Double,
    val change24h: Double,
    val direction: PriceDirection,
    val lastUpdated: Instant,
)

enum class Metal(val label: String, val unit: String) {
    GOLD("Gold", "oz"),
    SILVER("Silver", "oz"),
}

enum class PriceDirection { UP, DOWN }

/**
 * Price history point for chart rendering.
 */
data class PriceHistoryPoint(
    val date: String,
    val price: Double,
)

/**
 * Market news item.
 */
data class NewsItem(
    val id: String,
    val source: String,
    val headline: String,
    val summary: String,
    val url: String,
    val publishedAt: String, // relative: "2H AGO", "1D AGO"
)

/**
 * User profile.
 */
data class UserProfile(
    val id: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val primaryTimezone: String = "America/New_York",
    val secondaryTimezone: String = "Asia/Kolkata",
    val familyId: String? = null,
    val role: FamilyRole = FamilyRole.MEMBER,
)

enum class FamilyRole { ADMIN, MEMBER }

/**
 * Family member (simplified view).
 */
data class FamilyMember(
    val id: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val role: FamilyRole,
    val initials: String,
)

/**
 * Timezone coordination info.
 */
data class TimezoneInfo(
    val estTime: String,
    val istTime: String,
    val overlapStartHour: Int,
    val overlapEndHour: Int,
    val remainingOverlapHours: Int,
    val overlapProgress: Float, // 0f..1f progress through overlap window
)

/**
 * Dashboard briefing summary.
 */
data class DashboardBriefing(
    val dateDisplay: String,        // "Today" or day of week
    val dateSubtitle: String,       // "Oct 24"
    val timezone: TimezoneInfo,
    val urgentTasks: List<Task>,
    val urgentTaskCount: Int,
    val goldPrice: InvestmentPrice?,
    val silverPrice: InvestmentPrice?,
)
