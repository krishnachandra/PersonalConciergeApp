package com.davinci.app.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Timezone coordination widget.
 *
 * Displays EST and IST times side by side with an overlap progress bar.
 * Matches the wireframe's rounded-corner timezone cards.
 */
@Composable
fun TimezoneWidget(
    estTime: String,
    istTime: String,
    overlapProgress: Float,
    remainingHours: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(DavinciColors.Surface)
            .padding(24.dp)
    ) {
        // 1. Labels Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "INDIA (IST)",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.TextMuted,
                letterSpacing = 1.sp
            )
            Text(
                text = "NEW YORK (EST/EDT)",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.TextMuted,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Times Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = istTime,
                style = MaterialTheme.typography.headlineMedium,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = estTime,
                style = MaterialTheme.typography.headlineMedium,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Status Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusChip(text = "Working", isHighlight = true)
            StatusChip(text = "Personal", isHighlight = false, colorScheme = "Personal")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Overlap Visual
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "OVERLAP WINDOW",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
                letterSpacing = 1.sp
            )
            Text(
                text = "$remainingHours HRS REMAINING",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { overlapProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = DavinciColors.Primary,
            trackColor = DavinciColors.SurfaceVariant,
        )
    }
}

@Composable
private fun StatusChip(text: String, isHighlight: Boolean, colorScheme: String = "") {
    val bgColor = when {
        colorScheme == "Personal" -> Color(0xFFFFEBD5) // Light Orange
        isHighlight -> DavinciColors.Primary.copy(alpha = 0.1f)
        else -> DavinciColors.SurfaceVariant
    }
    val textColor = when {
        colorScheme == "Personal" -> Color(0xFFFB923C) // Vivid Orange
        isHighlight -> DavinciColors.Primary
        else -> DavinciColors.TextMuted
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}
