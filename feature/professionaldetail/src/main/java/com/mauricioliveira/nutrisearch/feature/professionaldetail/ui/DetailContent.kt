package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails

internal sealed interface DetailContent {
    data object Loading: DetailContent
    data class Error(val error: AppError): DetailContent
    data class Content(
        val details: ProfessionalDetails,
        val isRefreshing: Boolean,
        val showOfflineBanner: Boolean,
    ): DetailContent
}

internal fun ProfessionalDetailState.toDetailContent(): DetailContent = when {
    details != null -> DetailContent.Content(
        details = details,
        isRefreshing = isRefreshing,
        showOfflineBanner = error != null
    )
    error != null -> DetailContent.Error(error)
    else -> DetailContent.Loading
}