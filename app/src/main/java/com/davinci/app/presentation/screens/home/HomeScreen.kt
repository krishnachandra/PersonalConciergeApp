package com.davinci.app.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.presentation.screens.home.components.MarketPulseCard
import com.davinci.app.presentation.screens.home.components.TimezoneWidget
import com.davinci.app.presentation.screens.home.components.UrgentTasksSection
import com.davinci.app.presentation.theme.DavinciColors

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToInvestment: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DavinciColors.Background)
            .verticalScroll(scrollState)
    ) {
        // ─── Top Header ──────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
                style = MaterialTheme.typography.headlineMedium,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Oct 24", // Hardcoded per design
                style = MaterialTheme.typography.titleMedium,
                color = DavinciColors.TextMuted,
            )
        }

        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        // ─── Timezone Widget ─────────────────────────────────────
        TimezoneWidget(
            estTime = "08:30 AM",
            istTime = "07:00 PM",
            overlapProgress = 0.7f,
            remainingHours = 3,
            modifier = Modifier.padding(24.dp)
        )

        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        // ─── Urgent Tasks Header ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "URGENT TASKS",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.TextMuted,
                letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "3 Due",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.Primary,
            )
        }

        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        // ─── Urgent Tasks List ───────────────────────────────────
        UrgentTasksSection()

        // ─── Market Pulse Header ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DavinciColors.SurfaceVariant.copy(alpha=0.3f))
                .clickable { onNavigateToInvestment() }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MARKET PULSE",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.TextMuted,
                letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp),
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to Market Pulse",
                tint = DavinciColors.TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
        
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        // ─── Market Pulse Cards ──────────────────────────────────
        MarketPulseCard(
            goldPrice = "$2,045.10",
            goldChange = "+0.4%", 
            silverPrice = "$22.40",
            silverChange = "-1.2%",
            modifier = Modifier
                .background(DavinciColors.SurfaceVariant.copy(alpha=0.3f))
                .padding(24.dp)
        )
        
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
