package com.davinci.app.presentation.screens.timezone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowDropDown
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

data class TimezoneOption(val name: String, val zoneId: String, val label: String)

val availableTimezones = listOf(
    TimezoneOption("India", "Asia/Kolkata", "IST"),
    TimezoneOption("New York", "America/New_York", "EST/EDT"),
    TimezoneOption("Central", "America/Chicago", "CST/CDT"),
    TimezoneOption("Pacific", "America/Los_Angeles", "PST/PDT"),
    TimezoneOption("London", "Europe/London", "GMT/BST")
)

@Composable
fun WhatIfTimeWidget() {
    var sourceZone by remember { mutableStateOf(availableTimezones[0]) } // Default India
    var targetZone by remember { mutableStateOf(availableTimezones[1]) } // Default NY
    
    val istZone = ZoneId.of(sourceZone.zoneId)
    val estZone = ZoneId.of(targetZone.zoneId)
    
    val currentSource = LocalDateTime.now(istZone)
    val startOfDay = currentSource.toLocalDate().atStartOfDay()
    val initialMinutes = currentSource.toLocalTime().toSecondOfDay() / 60f
    
    var sliderMinutes by remember { mutableFloatStateOf(initialMinutes) }
    
    val selectedIstTime = startOfDay.plusMinutes(sliderMinutes.toLong())
    val selectedInstant = selectedIstTime.atZone(istZone).toInstant()
    val selectedEstTime = selectedInstant.atZone(estZone).toLocalDateTime()
    
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    val istFormatted = selectedIstTime.format(formatter)
    val estFormatted = selectedEstTime.format(formatter)
    
    fun getStatus(hour: Int): Pair<String, Color> {
        return when (hour) {
            in 9..18 -> "Working" to Color(0xFF10B981) // Green
            in 6..8, in 19..22 -> "Personal" to Color(0xFFF59E0B) // Orange/Yellow
            else -> "Sleeping" to Color(0xFF6B7280) // Gray
        }
    }
    
    val (istStatusText, istStatusColor) = getStatus(selectedIstTime.hour)
    val (estStatusText, estStatusColor) = getStatus(selectedEstTime.hour)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DavinciColors.Surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // 0. DST Info (Above labels)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daylight Saving has started (Mar 8 - Nov 1)",
                style = MaterialTheme.typography.labelSmall,
                color = DavinciColors.TextMuted,
                fontStyle = FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Labels Row with Dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimezoneDropdown(
                selected = sourceZone,
                onSelect = { sourceZone = it }
            )
            TimezoneDropdown(
                selected = targetZone,
                onSelect = { targetZone = it },
                alignEnd = true
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 2. Times Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = istFormatted,
                style = MaterialTheme.typography.headlineMedium,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = estFormatted,
                style = MaterialTheme.typography.headlineSmall,
                color = DavinciColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // 2.5 Clocks Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClockFace(
                hour = selectedIstTime.hour,
                minute = selectedIstTime.minute,
                size = 60.dp
            )
            ClockFace(
                hour = selectedEstTime.hour,
                minute = selectedEstTime.minute,
                size = 60.dp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 3. Status Badges Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBadge(text = istStatusText, color = istStatusColor)
            StatusBadge(text = estStatusText, color = estStatusColor)
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
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
                text = "Dynamic Window:",
                style = MaterialTheme.typography.labelMedium,
                color = DavinciColors.Primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Adjust the slider to see how zones coordinate in real-time.",
                style = MaterialTheme.typography.bodySmall,
                color = DavinciColors.Primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TimezoneDropdown(
    selected: TimezoneOption,
    onSelect: (TimezoneOption) -> Unit,
    alignEnd: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (alignEnd) {
                Text(
                    text = "${selected.name.uppercase()} (${selected.label})",
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.Primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = DavinciColors.Primary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = "${selected.name.uppercase()} (${selected.label})",
                    style = MaterialTheme.typography.labelSmall,
                    color = DavinciColors.Primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = DavinciColors.Primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(DavinciColors.Surface)
        ) {
            availableTimezones.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            "${option.name} (${option.label})",
                            color = if (option == selected) DavinciColors.Primary else DavinciColors.TextPrimary
                        ) 
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ClockFace(
    hour: Int,
    minute: Int,
    size: androidx.compose.ui.unit.Dp
) {
    val primaryColor = DavinciColors.Primary
    val mutedColor = DavinciColors.TextMuted
    val dividerColor = DavinciColors.Divider

    Canvas(modifier = Modifier.size(size)) {
        val center = Offset(size.toPx() / 2, size.toPx() / 2)
        val radius = size.toPx() / 2

        // Draw Clock Face Border
        drawCircle(
            color = dividerColor,
            radius = radius,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )

        // Draw Hour Markers (optional pins)
        for (i in 0 until 12) {
            val angle = (i * 30).toDouble()
            val startX = center.x + (radius - 5.dp.toPx()) * Math.cos(Math.toRadians(angle - 90)).toFloat()
            val startY = center.y + (radius - 5.dp.toPx()) * Math.sin(Math.toRadians(angle - 90)).toFloat()
            val endX = center.x + radius * Math.cos(Math.toRadians(angle - 90)).toFloat()
            val endY = center.y + radius * Math.sin(Math.toRadians(angle - 90)).toFloat()
            
            drawLine(
                color = mutedColor.copy(alpha = 0.3f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Calculate Hand Angles
        // Hour hand: 360/12 = 30 degrees per hour + 0.5 degrees per minute
        val hourAngle = (hour % 12) * 30f + (minute / 60f) * 30f - 90f
        // Minute hand: 360/60 = 6 degrees per minute
        val minuteAngle = minute * 6f - 90f

        // Draw Minute Hand (Muted)
        val minX = center.x + (radius * 0.8f) * Math.cos(Math.toRadians(minuteAngle.toDouble())).toFloat()
        val minY = center.y + (radius * 0.8f) * Math.sin(Math.toRadians(minuteAngle.toDouble())).toFloat()
        drawLine(
            color = mutedColor.copy(alpha = 0.5f),
            start = center,
            end = Offset(minX, minY),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Draw Hour Hand (Blue/Teal Primary)
        val hrX = center.x + (radius * 0.5f) * Math.cos(Math.toRadians(hourAngle.toDouble())).toFloat()
        val hrY = center.y + (radius * 0.5f) * Math.sin(Math.toRadians(hourAngle.toDouble())).toFloat()
        drawLine(
            color = primaryColor,
            start = center,
            end = Offset(hrX, hrY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Draw Center Point
        drawCircle(
            color = primaryColor,
            radius = 2.dp.toPx(),
            center = center
        )
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
