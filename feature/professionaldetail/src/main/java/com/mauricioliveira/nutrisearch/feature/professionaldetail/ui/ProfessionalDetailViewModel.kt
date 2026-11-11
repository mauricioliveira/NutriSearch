package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails
import com.mauricioliveira.nutrisearch.core.domain.model.asAppError
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import kotlinx.coroutines.Job
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
    private var refreshJob: Job? = null

    /**
     * Cache first: Room emits the saved profile straight away and again after the refresh writes it.
     * The screen rules live here, so the UI only renders what it is told.
     */
    val state: StateFlow<ProfessionalDetailState> = combine(
        repository.observeProfessional(id = professionalId),
        refreshStatus,
    ) { details, status ->
        toState(details, status)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfessionalDetailState.Loading,
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
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            refreshStatus.value = RefreshStatus(isRefreshing = true)
            val failure = repository.refreshProfessional(id = professionalId).exceptionOrNull()
            refreshStatus.value = RefreshStatus(
                isRefreshing = false,
                error = failure?.asAppError()
            )
        }
    }

    private fun toState(
        details: ProfessionalDetails?,
        status: RefreshStatus
    ): ProfessionalDetailState =
        when {
            details != null -> ProfessionalDetailState.Content(
                details = details,
                isRefreshing = status.isRefreshing,
                refreshError = status.error,
            )
            status.error != null -> ProfessionalDetailState.Error(status.error)
            else -> ProfessionalDetailState.Loading
        }
}