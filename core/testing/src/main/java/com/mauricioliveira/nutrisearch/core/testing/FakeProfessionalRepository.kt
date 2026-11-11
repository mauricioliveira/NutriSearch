package com.mauricioliveira.nutrisearch.core.testing

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeProfessionalRepository : ProfessionalRepository {

    val resultsBySort = mutableMapOf<ProfessionalSort, List<Professional>>()
    val requestedSorts = mutableListOf<ProfessionalSort>()

    val details = MutableStateFlow<ProfessionalDetails?>(null)
    var refreshError: AppError? = null
    var refreshCalls = 0
    var refreshGate: CompletableDeferred<Unit>? = null

    /** Static data fully loaded, so paging-testing's asSnapshot sees an idle end state. */
    private val loaded = LoadState.NotLoading(endOfPaginationReached = true)
        .let { LoadStates(refresh = it, prepend = it, append = it) }

    override fun searchProfessionals(sort: ProfessionalSort): Flow<PagingData<Professional>> {
        requestedSorts += sort
        return flowOf(PagingData.from(resultsBySort[sort].orEmpty(), sourceLoadStates = loaded))
    }

    override fun observeProfessional(id: Int): Flow<ProfessionalDetails?> = details

    override suspend fun refreshProfessional(id: Int): Result<Unit> {
        refreshCalls++
        refreshGate?.await()
        return refreshError?.let { Result.failure(it) } ?: Result.success(Unit)
    }
}