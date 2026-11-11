package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails

sealed interface ProfessionalDetailState {

    data object Loading : ProfessionalDetailState

    data class Error(val error: AppError) : ProfessionalDetailState

    data class Content(
        val details: ProfessionalDetails,
        val isRefreshing: Boolean,
        val refreshError: AppError?,
    ) : ProfessionalDetailState
}

sealed interface ProfessionalDetailIntent {
    data object Retry : ProfessionalDetailIntent
}