package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.ItemSnapshotList
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import com.mauricioliveira.nutrisearch.core.ui.component.ErrorView
import com.mauricioliveira.nutrisearch.core.ui.component.LoadingView
import com.mauricioliveira.nutrisearch.core.ui.component.StaleDataBanner
import com.mauricioliveira.nutrisearch.core.ui.error.messageRes
import com.mauricioliveira.nutrisearch.feature.professionals.R
import kotlinx.coroutines.launch
import com.mauricioliveira.nutrisearch.core.ui.R as CoreUiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfessionalsScreen(
    state: ProfessionalsState,
    professionals: LazyPagingItems<Professional>,
    onIntent: (ProfessionalsIntent) -> Unit,
    onProfessionalClick: (Int) -> Unit,
) {
    val listState = rememberLazyListState()
    val scrollToTopOnNewData = rememberScrollToTopOnNewData(professionals, listState)

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.professionals_title)) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SortDropDown(
                options = state.sortOptions,
                selected = state.sort,
                onSelected = { sort ->
                    if (sort != state.sort) {
                        scrollToTopOnNewData()
                        onIntent(ProfessionalsIntent.SortSelected(sort))
                    }
                },
            )

            when(val content = listContent(professionals.loadState, professionals.itemCount)) {
                ListContent.Loading -> LoadingView(modifier = Modifier.fillMaxSize())
                is ListContent.Error -> ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    message = stringResource(content.error.messageRes()),
                    onRetry = professionals::refresh
                )
                ListContent.Empty -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.empty_professionals))
                }
                is ListContent.Content -> {
                    content.refreshError?.let { error ->
                        StaleDataBanner(
                            error = error,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                    PullToRefreshBox(
                        modifier = Modifier.fillMaxSize(),
                        isRefreshing = content.isRefreshing,
                        onRefresh = professionals::refresh,
                    ) {
                        ProfessionalsList(
                            professionals = professionals,
                            listState = listState,
                            onProfessionalClick = onProfessionalClick,
                        )
                    }
                }
            }
        }
    }
}

/** Holds the list shown when the sort changed, to detect when the new sort's data arrives. */
private class PendingScrollToTop {
    var listBeforeSortChange: ItemSnapshotList<Professional>? = null
}

@Composable
private fun rememberScrollToTopOnNewData(
    professionals: LazyPagingItems<Professional>,
    listState: LazyListState,
): () -> Unit {
    val scope = rememberCoroutineScope()
    val pending = remember { PendingScrollToTop() }
    val items = professionals.itemSnapshotList // read here so a new snapshot recomposes

    SideEffect {
        val before = pending.listBeforeSortChange
        // Empty: the new sort is still loading (full-screen Loading); wait for its rows.
        if (before != null && items !== before && items.isNotEmpty()) {
            pending.listBeforeSortChange = null
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    return remember(professionals) {
        { pending.listBeforeSortChange = professionals.itemSnapshotList }
    }
}

@Composable
private fun ProfessionalsList(
    professionals: LazyPagingItems<Professional>,
    listState: LazyListState,
    onProfessionalClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = professionals.itemCount,
            key = professionals.itemKey { it.id },
            contentType = professionals.itemContentType { "professional" },
        ) { index ->
            val professional = professionals[index] ?: return@items
            ProfessionalCard(
                modifier = Modifier.animateItem(),
                professional = professional,
                onClick = { onProfessionalClick(professional.id) }
            )
        }

        item(key = "append_footer") {
            AppendFooter(
                state = professionals.loadState.append,
                onRetry = professionals::retry,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortDropDown(
    options: List<ProfessionalSort>,
    selected: ProfessionalSort,
    onSelected: (ProfessionalSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            value = stringResource(selected.labelRes),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(text = stringResource(R.string.sort_by)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { sort ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(sort.labelRes)) },
                    onClick = {
                        onSelected(sort)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
private fun AppendFooter(
    state: LoadState,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when(state) {
            LoadState.Loading -> CircularProgressIndicator()
            is LoadState.Error -> TextButton(onClick = onRetry) {
                Text(text = stringResource(CoreUiR.string.retry))
            }
            is LoadState.NotLoading -> Unit
        }
    }
}

private val ProfessionalSort.labelRes: Int
    get() = when(this) {
        ProfessionalSort.BEST_MATCH -> R.string.sort_best_match
        ProfessionalSort.RATING -> R.string.sort_rating
        ProfessionalSort.MOST_POPULAR -> R.string.sort_most_popular
    }

