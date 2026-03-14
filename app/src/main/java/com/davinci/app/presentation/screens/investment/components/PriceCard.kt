package com.davinci.app.presentation.screens.investment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Price card for gold or silver.
 * Matches wireframe's top price display.
 */
@Composable
fun PriceCard(
    label: String,
    price: String,
    change: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DavinciColors.Surface)
            .padding(16.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = DavinciColors.TextMuted,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = price,
            style = MaterialTheme.typography.headlineLarge,
            color = DavinciColors.TextPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = change,
            style = MaterialTheme.typography.bodySmall,
            color = if (isPositive) DavinciColors.Positive else DavinciColors.Negative,
            fontWeight = FontWeight.Medium,
        )
    }
}
