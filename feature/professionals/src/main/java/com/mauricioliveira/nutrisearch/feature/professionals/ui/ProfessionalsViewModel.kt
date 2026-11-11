package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfessionalsViewModel(
    private val repository: ProfessionalRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val sort: Flow<ProfessionalSort> = savedStateHandle
        .getStateFlow(KEY_SORT, ProfessionalSort.Default.name)
        .map { it.toSortOrDefault() }
        .distinctUntilChanged()

    val state: StateFlow<ProfessionalsState> = sort
        .map { ProfessionalsState(sort = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfessionalsState(
                sort = savedStateHandle.get<String>(KEY_SORT).toSortOrDefault(),
            ),
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val professionals: Flow<PagingData<Professional>> = sort
        .flatMapLatest { repository.searchProfessionals(it) }
        .cachedIn(viewModelScope)

    fun onIntent(intent: ProfessionalsIntent) {
        when(intent) {
            is ProfessionalsIntent.SortSelected -> {
                savedStateHandle[KEY_SORT] = intent.sort.name
            }
        }
    }

    private fun String?.toSortOrDefault(): ProfessionalSort =
        ProfessionalSort.entries.firstOrNull { it.name == this } ?: ProfessionalSort.Default

    internal companion object {
        const val KEY_SORT = "sort"
    }
}