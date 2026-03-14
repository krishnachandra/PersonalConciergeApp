package com.davinci.app.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.davinci.app.MainActivity
import com.davinci.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

/**
 * Firebase Cloud Messaging service for handling push notifications.
 *
 * Handles:
 * - Task reminders (urgent and general)
 * - Event/birthday reminders
 * - Market alerts
 * - New task assignments
 */
@AndroidEntryPoint
class DavinciFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send token to server via NotificationService
        // POST /notification-service/register-device { token }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val type = data["type"] ?: "general"
        val title = data["title"] ?: message.notification?.title ?: "Davinci"
        val body = data["body"] ?: message.notification?.body ?: ""

        val channelId = when (type) {
            "task_urgent", "task_overdue" -> "tasks_urgent"
            "task_assigned", "task_completed" -> "tasks_general"
            "event", "birthday" -> "events"
            "market" -> "market"
            else -> "tasks_general"
        }

        showNotification(channelId, title, body)
    }

    private fun showNotification(channelId: String, title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(
                if (channelId == "tasks_urgent") NotificationCompat.PRIORITY_HIGH
                else NotificationCompat.PRIORITY_DEFAULT
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
