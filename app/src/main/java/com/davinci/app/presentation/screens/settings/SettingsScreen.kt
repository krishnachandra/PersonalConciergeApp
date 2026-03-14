package com.davinci.app.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.presentation.components.AvatarChip
import com.davinci.app.presentation.theme.DavinciColors

/**
 * Settings Screen — following the same Airy Minimal Teal design.
 *
 * Profile section, grouped settings categories,
 * teal section headers, sign-out at bottom.
 */
@Composable
fun SettingsScreen(
    onSignOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DavinciColors.Background)
            .verticalScroll(rememberScrollState()),
    ) {
        // ─── Header ──────────────────────────────────────
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displayMedium,
            color = DavinciColors.TextPrimary,
            modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 24.dp),
        )

        HorizontalDivider(color = DavinciColors.Divider, thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Profile Section ─────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarChip(
                imageUrl = uiState.avatarUrl,
                initials = uiState.initials,
                size = 56.dp,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = uiState.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = DavinciColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = uiState.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DavinciColors.TextMuted,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ─── ACCOUNT ─────────────────────────────────────
        SettingsSectionHeader("ACCOUNT")
        SettingsRow("Profile") { /* Navigate to profile edit */ }
        SettingsRow("Family Members") { /* Navigate to family list */ }
        SettingsRow("Notification Preferences") { /* Navigate to notif prefs */ }

        Spacer(modifier = Modifier.height(24.dp))

        // ─── DATA & SYNC ────────────────────────────────
        SettingsSectionHeader("DATA & SYNC")
        SettingsRow("Timezone Settings") { /* Navigate to tz config */ }
        SettingsRow("Investment Preferences") { /* Navigate to invest prefs */ }
        SettingsRow("Data Export") { /* Trigger data export */ }

        Spacer(modifier = Modifier.height(24.dp))

        // ─── SECURITY ───────────────────────────────────
        SettingsSectionHeader("SECURITY")
        SettingsRow("Change Password") { /* Navigate to password change */ }

        // Biometric lock toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Biometric Lock",
                style = MaterialTheme.typography.bodyLarge,
                color = DavinciColors.TextPrimary,
            )
            Switch(
                checked = uiState.biometricEnabled,
                onCheckedChange = { viewModel.toggleBiometric(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DavinciColors.TextOnPrimary,
                    checkedTrackColor = DavinciColors.Primary,
                    uncheckedThumbColor = DavinciColors.TextMuted,
                    uncheckedTrackColor = DavinciColors.SurfaceVariant,
                ),
            )
        }

        SettingsRow("Two-Factor Authentication") { /* Navigate to 2FA setup */ }

        Spacer(modifier = Modifier.height(24.dp))

        // ─── ABOUT ──────────────────────────────────────
        SettingsSectionHeader("ABOUT")
        SettingsRow("App Version", subtitle = "1.0.0") { }
        SettingsRow("Privacy Policy") { /* Open browser */ }
        SettingsRow("Terms of Service") { /* Open browser */ }
        SettingsRow("Open Source Licenses") { /* Navigate to licenses */ }

        Spacer(modifier = Modifier.height(32.dp))

        // ─── Sign Out ───────────────────────────────────
        TextButton(
            onClick = {
                viewModel.signOut()
                onSignOut()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.labelLarge,
                color = DavinciColors.Error,
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = DavinciColors.Primary,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        letterSpacing = androidx.compose.ui.unit.TextUnit(
            1.5f, androidx.compose.ui.unit.TextUnitType.Sp
        ),
    )
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = DavinciColors.TextPrimary,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = DavinciColors.TextMuted,
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = DavinciColors.TextMuted,
            modifier = Modifier.size(20.dp),
        )
    }
}
