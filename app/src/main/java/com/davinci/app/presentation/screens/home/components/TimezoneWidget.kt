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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    Column(modifier = modifier) {
        // ─── Time Cards ──────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // EST card
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DavinciColors.Surface)
                    .padding(20.dp),
            ) {
                Text(
                    text = "EST",
                    style = MaterialTheme.typography.labelMedium,
                    color = DavinciColors.TextMuted,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = estTime,
                    style = MaterialTheme.typography.displaySmall,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            // IST card
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DavinciColors.Surface)
                    .padding(20.dp),
            ) {
                Text(
                    text = "IST",
                    style = MaterialTheme.typography.labelMedium,
                    color = DavinciColors.TextMuted,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = istTime,
                    style = MaterialTheme.typography.displaySmall,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ─── Overlap Bar ─────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "OVERLAP WINDOW",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
                letterSpacing = androidx.compose.ui.unit.TextUnit(
                    1.5f,
                    androidx.compose.ui.unit.TextUnitType.Sp
                ),
            )
            Text(
                text = "$remainingHours HRS REMAINING",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
                letterSpacing = androidx.compose.ui.unit.TextUnit(
                    1.5f,
                    androidx.compose.ui.unit.TextUnitType.Sp
                ),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar matching the teal gradient from wireframe
        LinearProgressIndicator(
            progress = { overlapProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = DavinciColors.Primary,
            trackColor = DavinciColors.SurfaceVariant,
        )
    }
}
