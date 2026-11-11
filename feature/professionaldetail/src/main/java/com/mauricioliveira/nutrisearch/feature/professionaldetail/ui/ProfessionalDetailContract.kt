package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails

data class ProfessionalDetailState(
    val details: ProfessionalDetails? = null,
    val isRefreshing: Boolean = true,
    val error: AppError? = null,
)

sealed interface ProfessionalDetailIntent {
    data object Retry: ProfessionalDetailIntent
}