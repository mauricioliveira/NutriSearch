package com.mauricioliveira.nutrisearch.feature.professionals.ui

import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort

data class ProfessionalsState(
    val sort: ProfessionalSort = ProfessionalSort.Default,
    val sortOptions: List<ProfessionalSort> = ProfessionalSort.entries
)

sealed interface ProfessionalsIntent{
    data class SortSelected(val sort: ProfessionalSort): ProfessionalsIntent
}