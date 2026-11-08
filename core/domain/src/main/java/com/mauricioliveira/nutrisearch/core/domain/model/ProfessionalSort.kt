package com.mauricioliveira.nutrisearch.core.domain.model

/** Sort options supported by the search endpoint. The API value is mapped in :core:data. */
enum class ProfessionalSort {
    BEST_MATCH,
    RATING,
    MOST_POPULAR,
    ;

    companion object {
        val Default = BEST_MATCH
    }
}
