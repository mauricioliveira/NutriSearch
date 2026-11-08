package com.mauricioliveira.nutrisearch.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mauricioliveira.nutrisearch.core.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Query("SELECT * FROM remote_keys WHERE sort_by = :sortBy")
    suspend fun get(sortBy: String): RemoteKeyEntity?

    @Upsert
    suspend fun upsert(key: RemoteKeyEntity)

    @Query("DELETE FROM remote_keys WHERE sort_by = :sortBy")
    suspend fun clear(sortBy: String)
}
