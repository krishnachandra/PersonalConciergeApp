package com.davinci.app.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.presentation.screens.home.components.MarketPulseCard
import com.davinci.app.presentation.screens.home.components.TimezoneWidget
import com.davinci.app.presentation.screens.home.components.UrgentTasksSection
import com.davinci.app.presentation.theme.DavinciColors
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToInvestment: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    var currentTime by remember { mutableStateOf(ZonedDateTime.now()) }
    var lastUpdatedTime by remember { mutableStateOf(ZonedDateTime.now()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = ZonedDateTime.now()
        }
    }

    val dayFormatter = remember { DateTimeFormatter.ofPattern("EEEE") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    val formattedDay = currentTime.format(dayFormatter)
    val formattedDate = currentTime.format(dateFormatter)
    val formattedTime = currentTime.format(timeFormatter)

    val lastUpdatedString = "last updated on ${lastUpdatedTime.format(dateFormatter)} ${lastUpdatedTime.format(timeFormatter)}"

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
            // Left Column: User & Location
            Column {
                Text(
                    text = "Hi NKC",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Location Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = DavinciColors.TextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Jubilee Hills, Hyderabad",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DavinciColors.TextMuted,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { lastUpdatedTime = ZonedDateTime.now() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Refresh location",
                            modifier = Modifier.size(16.dp),
                            tint = DavinciColors.TextMuted
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                // Weather Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Thermostat,
                        contentDescription = "Temperature",
                        modifier = Modifier.size(16.dp),
                        tint = DavinciColors.TextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "32°C • AQI 42",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DavinciColors.TextMuted,
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                // Last Updated
                Text(
                    text = lastUpdatedString,
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.TextMuted,
                    fontStyle = FontStyle.Italic
                )
            }

            // Right Column: Time & Date
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formattedDay,
                    style = MaterialTheme.typography.titleMedium,
                    color = DavinciColors.TextMuted,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.titleMedium,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DavinciColors.TextMuted,
                )
            }
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
