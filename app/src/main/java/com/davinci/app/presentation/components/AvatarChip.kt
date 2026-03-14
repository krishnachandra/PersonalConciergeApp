package com.davinci.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Circular avatar chip — shows user photo or initials.
 * Matches the wireframe avatar style (circular, small).
 */
@Composable
fun AvatarChip(
    imageUrl: String? = null,
    initials: String = "?",
    size: Dp = 36.dp,
    isSelected: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (isSelected) DavinciColors.Primary.copy(alpha = 0.15f) else DavinciColors.SurfaceVariant
    val textColor = if (isSelected) DavinciColors.Primary else DavinciColors.TextMuted

    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Avatar",
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .then(if (!isSelected) Modifier.background(Color.Gray.copy(alpha = 0.5f)) else Modifier),
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center,
        ) {
            val fontSize = if (size <= 24.dp) 8.sp else 12.sp
            Text(
                text = initials.take(3).uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                ),
                color = textColor,
            )
        }
    }
}
