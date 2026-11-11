package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ProfessionalDetailRoute(
    professionalId: Int,
    onBack: () -> Unit,
    viewModel: ProfessionalDetailViewModel = koinViewModel { parametersOf(professionalId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfessionalDetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onBack = onBack,
    )
}