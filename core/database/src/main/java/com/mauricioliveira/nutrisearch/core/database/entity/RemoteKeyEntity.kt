package com.mauricioliveira.nutrisearch.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Paging state per sort order. [nextOffset] is null once the end of the list was reached. */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey @ColumnInfo(name = "sort_by") val sortBy: String,
    @ColumnInfo(name = "next_offset") val nextOffset: Int?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long,
)
