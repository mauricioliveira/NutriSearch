package com.mauricioliveira.nutrisearch.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shared shape of the search item and the profile response.
 * [aboutMe] is only present in GET /professionals/:id.
 */
@Serializable
data class ProfessionalDto(
    val id: Int,
    val name: String,
    val rating: Int,
    @SerialName("rating_count") val ratingCount: Int,
    val languages: List<String> = emptyList(),
    val expertise: List<String> = emptyList(),
    @SerialName("profile_picture_url") val profilePictureUrl: String? = null,
    @SerialName("about_me") val aboutMe: String? = null,
)
