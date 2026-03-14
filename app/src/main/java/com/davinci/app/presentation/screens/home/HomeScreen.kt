package com.davinci.app.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shield
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.presentation.screens.home.components.CompletedTasksSection
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
    onNavigateToTimezone: () -> Unit,
    onLogout: () -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Menu row at the top right
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Menu Box
                Box {
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.offset(x = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = DavinciColors.TextPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(DavinciColors.Surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("IST to EST", color = DavinciColors.TextPrimary) },
                            onClick = {
                                menuExpanded = false
                                onNavigateToTimezone()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout", color = DavinciColors.TextPrimary) },
                            onClick = {
                                menuExpanded = false
                                onLogout()
                            }
                        )
                    }
                }
            }

            // Next Line: Greeting and Shield
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Hi! NKC",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = "Admin Shield",
                    tint = Color(0xFFFFD700), // Golden color
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Second Row: Location/Weather & Time/Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left Column: User & Location
                Column {
                    // Location Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = DavinciColors.EmojiRed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Jubilee Hills, Hyderabad",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DavinciColors.TextMuted,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Weather Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Thermostat,
                        contentDescription = "Temperature",
                        modifier = Modifier.size(16.dp),
                        tint = DavinciColors.EmojiRed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "32°C • AQI 42",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DavinciColors.TextMuted,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    // AQI Progress Bar
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(DavinciColors.SurfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.3f) // 42/500ish or just a representative width
                                .background(DavinciColors.AQI_Good)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Last Updated Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = "Updated time",
                        modifier = Modifier.size(16.dp),
                        tint = DavinciColors.TextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = lastUpdatedString,
                        style = MaterialTheme.typography.labelSmall,
                        color = DavinciColors.TextMuted,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { lastUpdatedTime = ZonedDateTime.now() },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Refresh data",
                            modifier = Modifier.size(12.dp),
                            tint = DavinciColors.Primary
                        )
                    }
                }
            }

            // Right Column: Date, Day, Time
            Column(horizontalAlignment = Alignment.End) {
                // 1. Date (Bold, First line)
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleMedium,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 2. Day (Grey)
                Text(
                    text = formattedDay,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DavinciColors.TextMuted,
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 3. Time (Grey)
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DavinciColors.TextMuted,
                )
            }
        }
        }

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

        Spacer(modifier = Modifier.height(32.dp))

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

        // ─── Categories Section ──────────────────────────────────
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "LIFESTYLE",
            style = MaterialTheme.typography.labelMedium,
            color = DavinciColors.TextMuted,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryTile("Family", Icons.Outlined.People, Modifier.weight(1f))
                CategoryTile("Health", Icons.Outlined.FavoriteBorder, Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryTile("Documents", Icons.Outlined.Description, Modifier.weight(1f), hasNotification = true)
                CategoryTile("My Home", Icons.Outlined.Home, Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryTile("Finance", Icons.Outlined.Payments, Modifier.weight(1f), hasNotification = true)
                CategoryTile("Milestones", Icons.Outlined.Flag, Modifier.weight(1f))
            }
        }
        
        // ─── Completed Tasks Header ─────────────────────────────────
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "COMPLETED TASKS",
            style = MaterialTheme.typography.labelMedium,
            color = DavinciColors.TextMuted,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold
        )
        
        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)
        
        CompletedTasksSection()

        // Extra large spacer at bottom so content doesn't get stuck behind Bottom Nav
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun CategoryTile(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    hasNotification: Boolean = false
) {
    Surface(
        onClick = { /* TODO */ },
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(8.dp),
        color = DavinciColors.Primary,
        tonalElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (hasNotification) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(10.dp)
                        .background(Color(0xFFFF9800), CircleShape) // Vibrant Orange dot
                        .align(Alignment.TopEnd)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
