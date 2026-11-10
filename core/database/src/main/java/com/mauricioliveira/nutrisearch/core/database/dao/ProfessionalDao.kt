package com.mauricioliveira.nutrisearch.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalEntity
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfessionalDao {

    /**
     * Grouped by id because offset paging can store the same professional at two positions
     * when the server's order shifts between pages; duplicate ids would crash a LazyColumn
     * keyed by id. Each professional keeps its first position.
     */
    @Query(
        """
        SELECT p.* FROM professionals p
        INNER JOIN search_results s ON s.professional_id = p.id
        WHERE s.sort_by = :sortBy
        GROUP BY p.id
        ORDER BY MIN(s.position) ASC
        """,
    )
    fun pagingSource(sortBy: String): PagingSource<Int, ProfessionalEntity>

    @Query("SELECT * FROM professionals WHERE id = :id")
    fun observeById(id: Int): Flow<ProfessionalEntity?>

    /**
     * Partial upsert: inserts new rows (about_me = null) and, for existing rows,
     * updates only the summary columns, so a cached about_me survives list refreshes.
     */
    @Upsert(entity = ProfessionalEntity::class)
    suspend fun upsertSummaries(summaries: List<ProfessionalSummaryEntity>)

    /** Full upsert, used by the profile endpoint which includes about_me. */
    @Upsert
    suspend fun upsert(professional: ProfessionalEntity)
}
