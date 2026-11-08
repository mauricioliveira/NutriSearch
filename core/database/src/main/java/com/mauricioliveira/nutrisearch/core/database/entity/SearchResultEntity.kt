package com.mauricioliveira.nutrisearch.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * Position of a professional inside the results of one sort order.
 * Keeps each sort's ordering separate while the professional rows are shared.
 */
@Entity(
    tableName = "search_results",
    primaryKeys = ["sort_by", "position"],
    indices = [Index("professional_id")],
)
data class SearchResultEntity(
    @ColumnInfo(name = "sort_by") val sortBy: String,
    val position: Int,
    @ColumnInfo(name = "professional_id") val professionalId: Int,
)
