package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ProfessionalsRoute(
    onProfessionalClick: (Int) -> Unit,
    viewModel: ProfessionalsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val professionals = viewModel.professionals.collectAsLazyPagingItems()

    ProfessionalsScreen(
        state = state,
        professionals = professionals,
        onIntent = viewModel::onIntent,
        onProfessionalClick = onProfessionalClick,
    )
}