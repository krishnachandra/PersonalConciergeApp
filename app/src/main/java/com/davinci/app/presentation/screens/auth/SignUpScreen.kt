package com.davinci.app.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onSignUpSuccess, modifier = Modifier.fillMaxWidth()) {
            Text("Complete Sign Up")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Login")
        }
    }
}
