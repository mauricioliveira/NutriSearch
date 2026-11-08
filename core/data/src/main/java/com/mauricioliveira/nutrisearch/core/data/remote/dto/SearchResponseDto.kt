package com.mauricioliveira.nutrisearch.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val professionals: List<ProfessionalDto>,
)
