package com.mauricioliveira.nutrisearch.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mauricioliveira.nutrisearch.core.database.entity.SearchResultEntity

@Dao
interface SearchResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<SearchResultEntity>)

    @Query("DELETE FROM search_results WHERE sort_by = :sortBy")
    suspend fun clear(sortBy: String)

    /** Professional ids for [sortBy] in position order. Used by tests to check what was stored. */
    @Query("SELECT professional_id FROM search_results WHERE sort_by = :sortBy ORDER BY position ASC")
    suspend fun idsFor(sortBy: String): List<Int>
}
