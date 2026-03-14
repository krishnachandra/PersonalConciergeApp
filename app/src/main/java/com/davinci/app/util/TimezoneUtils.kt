package com.davinci.app.util

import com.davinci.app.domain.model.TimezoneInfo
import java.time.*
import java.time.format.DateTimeFormatter

/**
 * Pure client-side timezone utilities for IST/EST coordination.
 * No network dependency — instant computation.
 */
object TimezoneUtils {

    private val EST = ZoneId.of("America/New_York")
    private val IST = ZoneId.of("Asia/Kolkata")
    private val TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a")

    // Typical overlap window: 9 AM EST to 9 PM IST
    // which is 9 AM - 11:30 AM EST / 7:30 PM - 10 PM IST (approx)
    private const val OVERLAP_START_EST = 9   // 9 AM EST
    private const val OVERLAP_END_EST = 12    // 12 PM EST (noon)

    fun getCurrentTimezoneInfo(): TimezoneInfo {
        val now = Instant.now()
        val estNow = now.atZone(EST)
        val istNow = now.atZone(IST)

        val estFormatted = estNow.format(TIME_FORMAT)
        val istFormatted = istNow.format(TIME_FORMAT)

        // Calculate overlap
        val currentEstHour = estNow.hour
        val remainingHours = when {
            currentEstHour < OVERLAP_START_EST -> OVERLAP_END_EST - OVERLAP_START_EST
            currentEstHour in OVERLAP_START_EST until OVERLAP_END_EST -> OVERLAP_END_EST - currentEstHour
            else -> 0
        }

        val overlapDuration = (OVERLAP_END_EST - OVERLAP_START_EST).toFloat()
        val elapsed = when {
            currentEstHour < OVERLAP_START_EST -> 0f
            currentEstHour >= OVERLAP_END_EST -> overlapDuration
            else -> (currentEstHour - OVERLAP_START_EST).toFloat()
        }
        val progress = (elapsed / overlapDuration).coerceIn(0f, 1f)

        return TimezoneInfo(
            estTime = estFormatted,
            istTime = istFormatted,
            overlapStartHour = OVERLAP_START_EST,
            overlapEndHour = OVERLAP_END_EST,
            remainingOverlapHours = remainingHours,
            overlapProgress = progress,
        )
    }
}
