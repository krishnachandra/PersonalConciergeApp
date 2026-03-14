package com.davinci.app.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davinci.app.presentation.theme.DavinciColors

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("nkc") }
    var password by remember { mutableStateOf("12345678") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DavinciColors.Background)
            .padding(horizontal = 24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(120.dp))

        // ─── App Title ───────────────────────────────────
        Text(
            text = "Davinci",
            style = MaterialTheme.typography.displayLarge,
            color = DavinciColors.TextPrimary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The All-in-One Family App",
            style = MaterialTheme.typography.bodyLarge,
            color = DavinciColors.Primary,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(72.dp))

        // ─── Username Field ─────────────────────────────────
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = {
                Text("Username", color = DavinciColors.TextMuted)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DavinciColors.Surface,
                unfocusedContainerColor = DavinciColors.Surface,
                focusedBorderColor = DavinciColors.Primary,
                unfocusedBorderColor = DavinciColors.Surface,
                cursorColor = DavinciColors.Primary,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ─── Password Field ──────────────────────────────
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text("Password", color = DavinciColors.TextMuted)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DavinciColors.Surface,
                unfocusedContainerColor = DavinciColors.Surface,
                focusedBorderColor = DavinciColors.Primary,
                unfocusedBorderColor = DavinciColors.Surface,
                cursorColor = DavinciColors.Primary,
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Outlined.VisibilityOff
                        } else {
                            Icons.Outlined.Visibility
                        },
                        contentDescription = "Toggle password",
                        tint = DavinciColors.TextMuted,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.login(username, password)
                }
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ─── Remember Me ───────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = DavinciColors.Primary,
                    checkmarkColor = DavinciColors.TextOnPrimary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Remember Me",
                style = MaterialTheme.typography.bodyMedium,
                color = DavinciColors.TextPrimary,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ─── Error Message ───────────────────────────────
        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                style = MaterialTheme.typography.bodySmall,
                color = DavinciColors.Error,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ─── Sign In Button ──────────────────────────────
        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DavinciColors.Primary,
                contentColor = DavinciColors.TextOnPrimary,
            ),
            enabled = !uiState.isLoading,
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = DavinciColors.TextOnPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Divider ─────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = DavinciColors.Divider,
            )
            Text(
                text = "  or  ",
                style = MaterialTheme.typography.bodySmall,
                color = DavinciColors.TextMuted,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = DavinciColors.Divider,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Google Sign In ──────────────────────────────
        OutlinedButton(
            onClick = { /* Disabled */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = false,
        ) {
            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // ─── Sign Up Link ────────────────────────────────
        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = DavinciColors.TextMuted.copy(alpha = 0.5f),
            )
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.bodyMedium,
                color = DavinciColors.Primary.copy(alpha = 0.5f),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
