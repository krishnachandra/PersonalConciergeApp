package com.davinci.app.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToInvestment: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToTasks, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Tasks")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToInvestment, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Investments")
        }
    }
}
