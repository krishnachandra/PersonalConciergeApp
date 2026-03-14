package com.davinci.app.presentation.screens.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Horizontal scrolling category tabs.
 * Matches wireframe: All | Personal | Purchases | Finances
 * Active tab has teal text + underline indicator.
 */
@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Adjusted for per-item padding
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stack text to reserve width for the bold version
                Box(contentAlignment = Alignment.Center) {
                    // Hidden bold text to reserve space
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = Color.Transparent,
                        maxLines = 1
                    )
                    // Visible text
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isSelected) DavinciColors.Primary else DavinciColors.TextMuted,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Underline indicator with consistent width handling
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(if (isSelected) DavinciColors.Primary else Color.Transparent)
                )
            }
        }
    }
}
