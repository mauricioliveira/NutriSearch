package com.mauricioliveira.nutrisearch.core.database.entity

import androidx.room.ColumnInfo

/**
 * The columns returned by the search endpoint (everything except about_me).
 * Used as a partial entity so list pages never overwrite a cached about_me.
 */
data class ProfessionalSummaryEntity(
    val id: Int,
    val name: String,
    val rating: Int,
    @ColumnInfo(name = "rating_count") val ratingCount: Int,
    val languages: List<String>,
    val expertise: List<String>,
    @ColumnInfo(name = "profile_picture_url") val profilePictureUrl: String?,
)