package com.mauricioliveira.nutrisearch.core.domain.model

/** Summary shown on the search list (GET /professionals/search). */
data class Professional(
    val id: Int,
    val name: String,
    val rating: Int,
    val ratingCount: Int,
    val languages: List<String>,
    val expertise: List<String>,
    val profilePictureUrl: String?,
)
