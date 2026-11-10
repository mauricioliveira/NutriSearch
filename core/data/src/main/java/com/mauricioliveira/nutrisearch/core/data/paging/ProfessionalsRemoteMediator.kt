package com.mauricioliveira.nutrisearch.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mauricioliveira.nutrisearch.core.data.mapper.toAppError
import com.mauricioliveira.nutrisearch.core.data.mapper.toSummaryEntity
import com.mauricioliveira.nutrisearch.core.data.remote.NutriSearchApi
import com.mauricioliveira.nutrisearch.core.database.NutriSearchDatabase
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalEntity
import com.mauricioliveira.nutrisearch.core.database.entity.RemoteKeyEntity
import com.mauricioliveira.nutrisearch.core.database.entity.SearchResultEntity
import kotlin.coroutines.cancellation.CancellationException

/**
 * Loads pages of one sort order from the API into Room.
 * The UI never reads from here; it reads Room's PagingSource, and this class keeps Room filled.
 */
@OptIn(ExperimentalPagingApi::class)
internal class ProfessionalsRemoteMediator(
    private val sortBy: String,
    private val api: NutriSearchApi,
    private val database: NutriSearchDatabase,
    private val cacheTimeoutMillis: Long = DEFAULT_CACHE_TIMEOUT_MILLIS,
    private val now: () -> Long = System::currentTimeMillis,
): RemoteMediator<Int, ProfessionalEntity>() {

    private val professionalDao = database.professionalDao()
    private val searchResultDao = database.searchResultDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = remoteKeyDao.get(sortBy)?.lastUpdated
            ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        return if (now() - lastUpdated < cacheTimeoutMillis) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProfessionalEntity>
    ): MediatorResult {
        val remoteKey = remoteKeyDao.get(sortBy)

        val offset = when(loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                 remoteKey?.nextOffset
                     ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }

        return try {
            val response = api.searchProfessionals(
                limit = state.config.pageSize,
                offset = offset,
                sortBy = sortBy,
            )

            val loadedUntil = offset + response.professionals.size
            val endReached = response.professionals.isEmpty() || loadedUntil >= response.count

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    searchResultDao.clear(sortBy)
                }

                professionalDao.upsertSummaries(
                    summaries = response.professionals.map { it.toSummaryEntity() }
                )

                searchResultDao.insertAll(
                    response.professionals.mapIndexed { index, dto ->
                        SearchResultEntity(
                            sortBy = sortBy,
                            position = offset + index,
                            professionalId = dto.id,
                        )
                    }
                )

                remoteKeyDao.upsert(
                    RemoteKeyEntity(
                        sortBy = sortBy,
                        nextOffset = if (endReached) null else loadedUntil,
                        lastUpdated =
                            if (loadType == LoadType.REFRESH) {
                                now()
                            } else {
                                remoteKey?.lastUpdated ?: now()
                            }
                    )
                )

                MediatorResult.Success(endOfPaginationReached = endReached)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e.toAppError())
        }
    }

    private companion object {
        const val DEFAULT_CACHE_TIMEOUT_MILLIS = 10 * 60 * 1000L // 10 minutes
    }
}