package com.davinci.app.presentation.screens.investment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.domain.model.PriceHistoryPoint
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Performance chart with time-range selector.
 *
 * Matches wireframe: "Performance" header, 1W/1M/1Y/ALL pills,
 * and line chart placeholder.
 *
 * NOTE: For the actual chart, integrate Vico library.
 * This provides the layout structure + placeholder.
 */
@Composable
fun PerformanceChart(
    selectedRange: String,
    onRangeSelected: (String) -> Unit,
    priceHistory: List<PriceHistoryPoint>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Header + range selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Performance",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DavinciColors.TextPrimary,
                )
                Text(
                    text = "Gold - 30 Days",
                    style = MaterialTheme.typography.bodySmall,
                    color = DavinciColors.TextMuted,
                )
            }

            // Range pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                listOf("1W", "1M", "1Y", "ALL").forEach { range ->
                    val isSelected = range == selectedRange
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isSelected) DavinciColors.TextPrimary
                                else DavinciColors.Surface
                            )
                            .clickable { onRangeSelected(range) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = range,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) DavinciColors.TextOnPrimary
                            else DavinciColors.TextMuted,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Chart area
        // TODO: Replace with Vico CartesianChartHost for actual charting
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DavinciColors.Surface),
            contentAlignment = Alignment.Center,
        ) {
            if (priceHistory.isEmpty()) {
                Text(
                    text = "Loading chart data...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DavinciColors.TextMuted,
                )
            } else {
                // Placeholder — Vico chart integration goes here
                Text(
                    text = "${priceHistory.size} data points",
                    style = MaterialTheme.typography.bodySmall,
                    color = DavinciColors.TextMuted,
                )
            }
        }
    }
}
