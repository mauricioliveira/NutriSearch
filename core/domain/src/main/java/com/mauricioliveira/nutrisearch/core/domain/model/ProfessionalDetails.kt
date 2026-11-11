package com.mauricioliveira.nutrisearch.core.domain.model

/** Full profile (GET /professionals/:id). */
data class ProfessionalDetails(
    val professional: Professional,
    val aboutMe: String?,
)
