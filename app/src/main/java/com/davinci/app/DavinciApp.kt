package com.davinci.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DavinciApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val manager = getSystemService(NotificationManager::class.java)

        val channels = listOf(
            NotificationChannel(
                "tasks_urgent",
                "Urgent Tasks",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for urgent and overdue tasks"
            },
            NotificationChannel(
                "tasks_general",
                "Task Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General task updates and assignments"
            },
            NotificationChannel(
                "events",
                "Events & Birthdays",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Event and birthday reminders"
            },
            NotificationChannel(
                "market",
                "Market Alerts",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Gold and silver market movement alerts"
            }
        )

        channels.forEach { manager.createNotificationChannel(it) }
    }
}
