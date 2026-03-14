package com.davinci.app.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Davinci Design System — Color Palette
 * Derived from the "Airy Minimal Teal" wireframes.
 */
object DavinciColors {
    // ─── Core Palette ────────────────────────────────────────
    val Primary         = Color(0xFF0D9488)   // Teal — buttons, active states, FAB
    val PrimaryDark     = Color(0xFF0A7B71)   // Darker teal for pressed states
    val PrimaryLight    = Color(0xFFF0FDFA)   // Light teal — accent backgrounds

    // ─── Neutral ─────────────────────────────────────────────
    val Background      = Color(0xFFFFFFFF)   // Pure white canvas
    val Surface         = Color(0xFFF9FAFB)   // Off-white — inputs, cards, timezone widget
    val SurfaceVariant  = Color(0xFFF3F4F6)   // Slightly deeper surface for dividers

    // ─── Text ────────────────────────────────────────────────
    val TextPrimary     = Color(0xFF111827)   // Deep charcoal — headings, body
    val TextSecondary   = Color(0xFF4B5563)   // Medium gray — category labels
    val TextMuted       = Color(0xFF9CA3AF)   // Muted — timestamps, secondary info
    val TextOnPrimary   = Color(0xFFFFFFFF)   // White text on teal backgrounds

    // ─── Semantic ────────────────────────────────────────────
    val Positive        = Color(0xFF0D9488)   // Matches primary — positive deltas
    val Negative        = Color(0xFFEF4444)   // Red — negative deltas, urgent markers
    val Error           = Color(0xFFDC2626)   // Destructive actions, error states
    val Warning         = Color(0xFFF59E0B)   // Urgent triangle badge color

    // ─── Misc ────────────────────────────────────────────────
    val Divider         = Color(0xFFE5E7EB)   // Subtle list dividers
    val Overlay         = Color(0x40000000)   // Scrim for bottom sheets
    val Completed       = Color(0xFF9CA3AF)   // Completed task text (struck through)
    val CompletedCheck  = Color(0xFF60A5FA)   // Blue checkmark for completed items
}
