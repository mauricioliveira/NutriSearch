package com.mauricioliveira.nutrisearch.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalEntity
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
     * Upserts list results without wiping an already cached about_me,
     * since the search endpoint does not return it.
     */
    @Query(
        """
        INSERT INTO professionals (id, name, rating, rating_count, languages, expertise, profile_picture_url)
        VALUES (:id, :name, :rating, :ratingCount, :languages, :expertise, :profilePictureUrl)
        ON CONFLICT(id) DO UPDATE SET
            name = excluded.name,
            rating = excluded.rating,
            rating_count = excluded.rating_count,
            languages = excluded.languages,
            expertise = excluded.expertise,
            profile_picture_url = excluded.profile_picture_url
        """,
    )
    suspend fun upsertSummary(
        id: Int,
        name: String,
        rating: Int,
        ratingCount: Int,
        languages: List<String>,
        expertise: List<String>,
        profilePictureUrl: String?,
    )

    /** Full upsert, used by the profile endpoint which includes about_me. */
    @Upsert
    suspend fun upsert(professional: ProfessionalEntity)
}
