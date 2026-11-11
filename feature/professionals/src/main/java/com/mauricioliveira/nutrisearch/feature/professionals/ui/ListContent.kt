package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.domain.model.asAppError

internal sealed interface ListContent {
    data object Loading : ListContent
    data class Error(val error: AppError) : ListContent
    data object Empty : ListContent
    data class Content(val isRefreshing: Boolean, val refreshError: AppError?) : ListContent
}

internal fun listContent(loadState: CombinedLoadStates, itemCount: Int): ListContent {
    val mediatorRefresh = loadState.mediator?.refresh
    val refresh = loadState.refresh
    return when {
        itemCount > 0 -> ListContent.Content(
            isRefreshing = mediatorRefresh is LoadState.Loading,
            refreshError = (mediatorRefresh as? LoadState.Error)?.error?.asAppError(),
        )
        refresh is LoadState.Error -> ListContent.Error(refresh.error.asAppError())
        refresh is LoadState.Loading -> ListContent.Loading
        loadState.append.endOfPaginationReached -> ListContent.Empty
        else -> ListContent.Loading
    }
}