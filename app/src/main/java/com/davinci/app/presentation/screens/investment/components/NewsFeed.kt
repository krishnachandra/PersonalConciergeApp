package com.davinci.app.presentation.screens.investment.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.domain.model.NewsItem
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Market briefing news feed.
 * Matches wireframe: source label, timestamp, headline, summary.
 */
@Composable
fun NewsFeed(
    news: List<NewsItem>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (news.isEmpty()) {
            Text(
                text = "No market news available.",
                style = MaterialTheme.typography.bodyMedium,
                color = DavinciColors.TextMuted,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        } else {
            news.forEach { item ->
                NewsRow(item = item)
                HorizontalDivider(
                    color = DavinciColors.Divider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun NewsRow(
    item: NewsItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        // Source + timestamp
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = item.source.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.Primary,
                fontWeight = FontWeight.Medium,
                letterSpacing = androidx.compose.ui.unit.TextUnit(
                    1.0f, androidx.compose.ui.unit.TextUnitType.Sp
                ),
            )
            Text(
                text = item.publishedAt.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Headline
        Text(
            text = item.headline,
            style = MaterialTheme.typography.bodyLarge,
            color = DavinciColors.TextPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Summary
        Text(
            text = item.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = DavinciColors.TextMuted,
            maxLines = 2,
        )
    }
}
