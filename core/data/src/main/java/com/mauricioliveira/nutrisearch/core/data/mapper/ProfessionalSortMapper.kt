package com.mauricioliveira.nutrisearch.core.data.mapper

import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort

/** Value sent as `sort_by`, also used as the cache key for each sort's results. */
internal val ProfessionalSort.apiValue: String
    get() = when (this) {
        ProfessionalSort.BEST_MATCH -> "best_match"
        ProfessionalSort.RATING -> "rating"
        ProfessionalSort.MOST_POPULAR -> "most_popular"
    }
