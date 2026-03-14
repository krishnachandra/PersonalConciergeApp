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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Horizontal scrolling category tabs.
 * Matches wireframe: All | Mine | Admin | Groceries | Finance
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
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory

            Column(
                modifier = Modifier
                    .clickable { onCategorySelected(category) }
                    .padding(vertical = 8.dp),
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) DavinciColors.Primary else DavinciColors.TextMuted,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Underline indicator
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(DavinciColors.Primary)
                    )
                } else {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}
