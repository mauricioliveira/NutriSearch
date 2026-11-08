package com.mauricioliveira.nutrisearch.core.domain.repository

import androidx.paging.PagingData
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import kotlinx.coroutines.flow.Flow

interface ProfessionalRepository {

    /** Paged results for [sort], backed by the local cache and refreshed from the network. */
    fun searchProfessionals(sort: ProfessionalSort): Flow<PagingData<Professional>>

    /** Emits the cached profile (or null if never fetched) and every later update. */
    fun observeProfessional(id: Int): Flow<ProfessionalDetails?>

    /** Fetches the profile from the network and stores it in the cache. */
    suspend fun refreshProfessional(id: Int): Result<Unit>
}
