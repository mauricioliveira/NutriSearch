package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mauricioliveira.nutrisearch.feature.professionals.R

// Placeholder: replaced by the paged list, sort filter and cards.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfessionalsScreen(onProfessionalClick: (Int) -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.professionals_title)) }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { onProfessionalClick(4) }) { Text("Open professional #4") }
        }
    }
}
