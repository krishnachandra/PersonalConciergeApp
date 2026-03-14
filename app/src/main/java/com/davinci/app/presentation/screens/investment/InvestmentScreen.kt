package com.davinci.app.presentation.screens.investment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.presentation.screens.investment.components.NewsFeed
import com.davinci.app.presentation.screens.investment.components.PerformanceChart
import com.davinci.app.presentation.screens.investment.components.PriceCard
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Investment Screen — Precious metals tracker.
 *
 * Matches wireframe: price cards, performance chart with
 * time-range selector, market briefing news feed.
 */
@Composable
fun InvestmentScreen(
    viewModel: InvestmentViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DavinciColors.Background)
            .verticalScroll(rememberScrollState()),
    ) {
        // ─── Header ──────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Investments",
                style = MaterialTheme.typography.displayMedium,
                color = DavinciColors.TextPrimary,
            )
            IconButton(onClick = { /* TODO: overflow menu */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = DavinciColors.TextPrimary,
                )
            }
        }

        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // ─── Price Cards ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PriceCard(
                label = "GOLD / OZ",
                price = uiState.goldPrice,
                change = uiState.goldChange,
                isPositive = uiState.goldPositive,
                modifier = Modifier.weight(1f),
            )
            PriceCard(
                label = "SILVER / OZ",
                price = uiState.silverPrice,
                change = uiState.silverChange,
                isPositive = uiState.silverPositive,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ─── Performance Chart ───────────────────────────
        PerformanceChart(
            selectedRange = uiState.selectedRange,
            onRangeSelected = { viewModel.selectRange(it) },
            priceHistory = uiState.priceHistory,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ─── Market Briefing ─────────────────────────────
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        Text(
            text = "MARKET BRIEFING",
            style = MaterialTheme.typography.labelSmall,
            color = DavinciColors.TextMuted,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            letterSpacing = androidx.compose.ui.unit.TextUnit(
                1.5f, androidx.compose.ui.unit.TextUnitType.Sp
            ),
        )

        NewsFeed(
            news = uiState.news,
            modifier = Modifier.padding(horizontal = 24.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
