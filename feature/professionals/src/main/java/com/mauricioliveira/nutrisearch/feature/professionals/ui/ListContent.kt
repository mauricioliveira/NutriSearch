package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

internal sealed interface ListContent {
    data object Loading : ListContent
    data class Error(val error: Throwable) : ListContent
    data object Empty : ListContent
    data class Content(val isRefreshing: Boolean, val showOfflineBanner: Boolean) : ListContent
}

internal fun listContent(loadState: CombinedLoadStates, itemCount: Int): ListContent {
    val mediatorRefresh = loadState.mediator?.refresh
    val refresh = loadState.refresh
    return when {
        itemCount > 0 -> ListContent.Content(
            isRefreshing = mediatorRefresh is LoadState.Loading,
            showOfflineBanner = mediatorRefresh is LoadState.Error,
        )
        refresh is LoadState.Error -> ListContent.Error(refresh.error)
        refresh is LoadState.Loading -> ListContent.Loading
        loadState.append.endOfPaginationReached -> ListContent.Empty
        else -> ListContent.Loading
    }
}