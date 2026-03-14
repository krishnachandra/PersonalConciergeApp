package com.davinci.app.presentation.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Market pulse card showing gold and silver prices.
 *
 * Matches the wireframe's bottom section: two columns with
 * price, metal name, and % change indicator.
 */
@Composable
fun MarketPulseCard(
    goldPrice: String,
    goldChange: String,
    silverPrice: String,
    silverChange: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        // Gold column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "GOLD",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = goldPrice,
                style = MaterialTheme.typography.headlineLarge,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(2.dp))

            val goldPositive = goldChange.contains("+")
            Text(
                text = if(goldPositive) "↑ $goldChange" else "↓ $goldChange",
                style = MaterialTheme.typography.bodyMedium,
                color = if (goldPositive) DavinciColors.Positive else DavinciColors.Negative,
            )
        }

        VerticalDivider(
            modifier = Modifier.height(72.dp),
            color = DavinciColors.Divider,
            thickness = 0.5.dp,
        )

        Spacer(modifier = Modifier.width(24.dp))

        // Silver column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "SILVER",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = silverPrice,
                style = MaterialTheme.typography.headlineLarge,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(2.dp))

            val silverPositive = silverChange.contains("+")
            Text(
                text = if(silverPositive) "↑ $silverChange" else "↓ $silverChange",
                style = MaterialTheme.typography.bodyMedium,
                color = if (silverPositive) DavinciColors.Positive else DavinciColors.Negative,
            )
        }
    }
}
