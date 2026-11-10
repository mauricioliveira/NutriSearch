package com.mauricioliveira.nutrisearch.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mauricioliveira.nutrisearch.core.data.mapper.apiValue
import com.mauricioliveira.nutrisearch.core.data.mapper.toAppError
import com.mauricioliveira.nutrisearch.core.data.mapper.toDetailsOrNull
import com.mauricioliveira.nutrisearch.core.data.mapper.toDomain
import com.mauricioliveira.nutrisearch.core.data.mapper.toEntity
import com.mauricioliveira.nutrisearch.core.data.paging.ProfessionalsRemoteMediator
import com.mauricioliveira.nutrisearch.core.data.remote.NutriSearchApi
import com.mauricioliveira.nutrisearch.core.database.NutriSearchDatabase
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

internal class DefaultProfessionalRepository(
    private val api: NutriSearchApi,
    private val database: NutriSearchDatabase,
): ProfessionalRepository {

    private val professionalDao = database.professionalDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun searchProfessionals(sort: ProfessionalSort): Flow<PagingData<Professional>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = ProfessionalsRemoteMediator(
                sortBy = sort.apiValue,
                api = api,
                database = database
            ),
            pagingSourceFactory = { professionalDao.pagingSource(sort.apiValue) },
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override fun observeProfessional(id: Int): Flow<ProfessionalDetails?> =
        professionalDao.observeById(id)
            .map { it?.toDetailsOrNull() }
            .distinctUntilChanged()

    override suspend fun refreshProfessional(id: Int): Result<Unit> =
        try {
            val dto = api.getProfessional(id)
            professionalDao.upsert(dto.toEntity())
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toAppError())
        }

    private companion object {
        const val PAGE_SIZE = 4
    }
}