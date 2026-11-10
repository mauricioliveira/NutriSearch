package com.mauricioliveira.nutrisearch.core.testing

import androidx.paging.PagingData
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeProfessionalRepository : ProfessionalRepository {

    val resultsBySort = mutableMapOf<ProfessionalSort, List<Professional>>()
    val requestedSorts = mutableListOf<ProfessionalSort>()

    val details = MutableStateFlow<ProfessionalDetails?>(null)
    var refreshError: AppError? = null
    var refreshCalls = 0

    override fun searchProfessionals(sort: ProfessionalSort): Flow<PagingData<Professional>> {
        requestedSorts += sort
        return flowOf(PagingData.from(resultsBySort[sort].orEmpty()))
    }

    override fun observeProfessional(id: Int): Flow<ProfessionalDetails?> = details

    override suspend fun refreshProfessional(id: Int): Result<Unit> {
        refreshCalls++
        return refreshError?.let { Result.failure(it) } ?: Result.success(Unit)
    }
}