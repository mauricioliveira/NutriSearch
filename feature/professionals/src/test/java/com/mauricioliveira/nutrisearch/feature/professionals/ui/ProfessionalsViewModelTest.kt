package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.lifecycle.SavedStateHandle
import androidx.paging.testing.asSnapshot
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import com.mauricioliveira.nutrisearch.core.testing.FakeProfessionalRepository
import com.mauricioliveira.nutrisearch.core.testing.MainDispatcherRule
import com.mauricioliveira.nutrisearch.core.testing.aProfessional
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProfessionalsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeProfessionalRepository().apply {
        resultsBySort[ProfessionalSort.BEST_MATCH] = listOf(
            aProfessional(id = 1)
        )

        resultsBySort[ProfessionalSort.RATING] = listOf(
            aProfessional(id = 2),
            aProfessional(id = 3),
        )

        resultsBySort[ProfessionalSort.MOST_POPULAR] = listOf(
            aProfessional(id = 4)
        )
    }

    private fun viewModel(savedStateHandle: SavedStateHandle = SavedStateHandle()) =
        ProfessionalsViewModel(repository = repository, savedStateHandle = savedStateHandle)

    @Test
    fun `starts with best match and shows its results`() = runTest {
        val viewModel = viewModel()

        assertEquals(ProfessionalSort.BEST_MATCH, viewModel.state.value.sort)
        assertEquals(listOf(1), viewModel.professionals.asSnapshot().map { it.id })
    }

    @Test
    fun `selecting a sort saves it in the saved state handle`() = runTest {
        val handle = SavedStateHandle()
        val viewModel = viewModel(handle)

        viewModel.onIntent(ProfessionalsIntent.SortSelected(ProfessionalSort.RATING))
        assertEquals("RATING", handle.get<String>(ProfessionalsViewModel.KEY_SORT))
    }

    @Test
    fun `selecting a sort updates the state and loads that sort's results`() = runTest {
        val viewModel = viewModel()

        viewModel.onIntent(ProfessionalsIntent.SortSelected(ProfessionalSort.RATING))
        assertEquals(listOf(2, 3), viewModel.professionals.asSnapshot().map { it.id })
        assertEquals(ProfessionalSort.RATING, viewModel.state.value.sort)
        assertEquals(ProfessionalSort.RATING, repository.requestedSorts.last())
    }

    @Test
    fun `restores the sort from the saved state handle`() = runTest {
        val handle = SavedStateHandle(mapOf(ProfessionalsViewModel.KEY_SORT to "MOST_POPULAR"))
        val viewModel = viewModel(handle)

        assertEquals(ProfessionalSort.MOST_POPULAR, viewModel.state.value.sort)
        assertEquals(listOf(4), viewModel.professionals.asSnapshot().map { it.id })
    }
}