package com.davinci.app.presentation.screens.timezone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimezoneScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("What If - IST to EST") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DavinciColors.Background,
                    titleContentColor = DavinciColors.TextPrimary,
                    navigationIconContentColor = DavinciColors.TextPrimary
                )
            )
        },
        containerColor = DavinciColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            WhatIfTimeWidget()
        }
    }
}

@Composable
fun WhatIfTimeWidget() {
    val istZone = ZoneId.of("Asia/Kolkata")
    val estZone = ZoneId.of("America/New_York")
    
    // Default to current IST time
    val currentIst = LocalDateTime.now(istZone)
    val startOfDay = currentIst.toLocalDate().atStartOfDay()
    val initialMinutes = currentIst.toLocalTime().toSecondOfDay() / 60f
    
    var sliderMinutes by remember { mutableFloatStateOf(initialMinutes) }
    
    val selectedIstTime = startOfDay.plusMinutes(sliderMinutes.toLong())
    val selectedInstant = selectedIstTime.atZone(istZone).toInstant()
    val selectedEstTime = selectedInstant.atZone(estZone).toLocalDateTime()
    
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    val istFormatted = selectedIstTime.format(formatter)
    val estFormatted = selectedEstTime.format(formatter)
    
    // Status badges logic
    val istHour = selectedIstTime.hour
    val estHour = selectedEstTime.hour
    
    fun getStatus(hour: Int): Pair<String, Color> {
        return when (hour) {
            in 9..18 -> "Working" to Color(0xFF10B981) // Green
            in 6..8, in 19..22 -> "Personal" to Color(0xFFF59E0B) // Orange/Yellow
            else -> "Sleeping" to Color(0xFF6B7280) // Gray
        }
    }
    
    val (istStatusText, istStatusColor) = getStatus(istHour)
    val (estStatusText, estStatusColor) = getStatus(estHour)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DavinciColors.Surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Meeting \"What-If\"",
            style = MaterialTheme.typography.titleLarge,
            color = DavinciColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Slide to adjust IST time",
            style = MaterialTheme.typography.bodyMedium,
            color = DavinciColors.TextMuted
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // IST Column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "INDIA (IST)",
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = istFormatted,
                    style = MaterialTheme.typography.headlineMedium,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatusBadge(text = istStatusText, color = istStatusColor)
            }
            
            // EST Column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "NEW YORK (EST/EDT)",
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = estFormatted,
                    style = MaterialTheme.typography.headlineMedium,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatusBadge(text = estStatusText, color = estStatusColor)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Slider(
            value = sliderMinutes,
            onValueChange = { sliderMinutes = it },
            valueRange = 0f..(24f * 60f - 1f),
            colors = SliderDefaults.colors(
                thumbColor = DavinciColors.Primary,
                activeTrackColor = DavinciColors.Primary,
                inactiveTrackColor = DavinciColors.Divider
            )
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(DavinciColors.Primary.copy(alpha = 0.1f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Golden Window:",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.Primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "6:30 PM - 9:30 PM IST / 9:00 AM - 12:00 PM EDT",
                style = MaterialTheme.typography.bodySmall,
                color = DavinciColors.Primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StatusBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
