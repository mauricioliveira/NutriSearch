package com.mauricioliveira.nutrisearch.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One row per professional, shared by every sort order.
 * [aboutMe] is null until the profile screen has fetched GET /professionals/:id.
 */
@Entity(tableName = "professionals")
data class ProfessionalEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val rating: Int,
    @ColumnInfo(name = "rating_count") val ratingCount: Int,
    val languages: List<String>,
    val expertise: List<String>,
    @ColumnInfo(name = "profile_picture_url") val profilePictureUrl: String?,
    @ColumnInfo(name = "about_me") val aboutMe: String? = null,
)
