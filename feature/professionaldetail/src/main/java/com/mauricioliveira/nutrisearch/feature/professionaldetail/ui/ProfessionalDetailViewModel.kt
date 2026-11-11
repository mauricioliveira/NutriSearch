package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfessionalDetailViewModel(
    private val professionalId: Int,
    private val repository: ProfessionalRepository,
): ViewModel() {

    /** Outcome of the last network refresh; the profile itself always come from Room */
    private data class RefreshStatus(
        val isRefreshing: Boolean = true,
        val error: AppError? = null,
    )

    private val refreshStatus = MutableStateFlow(RefreshStatus())

    /**
     * Derived from its two sources: the cached profile (Room) and the refresh status.
     * Cache first: Room emits the saved profile straight away and again after refresh writes it
     */
    val state: StateFlow<ProfessionalDetailState> = combine(
        repository.observeProfessional(id = professionalId),
        refreshStatus
    ) { details, status ->
        ProfessionalDetailState(
            details = details,
            isRefreshing = status.isRefreshing,
            error = status.error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfessionalDetailState()
    )

    init {
        refresh()
    }

    fun onIntent(intent: ProfessionalDetailIntent) {
        when(intent) {
            ProfessionalDetailIntent.Retry -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            refreshStatus.value = RefreshStatus(isRefreshing = true)
            val failure = repository.refreshProfessional(id = professionalId).exceptionOrNull()
            refreshStatus.value = RefreshStatus(
                isRefreshing = false,
                error = failure?.let { it as? AppError ?: AppError.Unknown(it) }
            )
        }
    }
}