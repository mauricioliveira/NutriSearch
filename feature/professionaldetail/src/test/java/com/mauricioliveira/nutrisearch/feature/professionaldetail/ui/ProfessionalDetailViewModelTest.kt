package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.testing.FakeProfessionalRepository
import com.mauricioliveira.nutrisearch.core.testing.MainDispatcherRule
import com.mauricioliveira.nutrisearch.core.testing.aProfessional
import com.mauricioliveira.nutrisearch.core.testing.aProfessionalDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ProfessionalDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeProfessionalRepository()

    private fun viewModel() = ProfessionalDetailViewModel(
        professionalId = 4,
        repository = repository
    )

    private suspend fun ProfessionalDetailViewModel.loadedState() = state.first { !it.isRefreshing }

    @Test
    fun `shows the cached profile and refreshes it on open`() = runTest {
        repository.details.value = aProfessionalDetails()

        val state = viewModel().loadedState()
        assertEquals(aProfessionalDetails(), state.details)
        assertNull(state.error)
        assertEquals(1, repository.refreshCalls)
    }

    @Test
    fun `a failed refresh keeps the cached profile and exposes the error`() = runTest {
        repository.details.value = aProfessionalDetails()
        repository.refreshError = AppError.NoConnection

        val state = viewModel().loadedState()
        assertEquals(aProfessionalDetails(), state.details)
        assertEquals(AppError.NoConnection, state.error)
    }

    @Test
    fun `retry refresh again and clears the error`() = runTest {
        repository.refreshError = AppError.NoConnection
        val viewModel = viewModel()
        assertEquals(AppError.NoConnection, viewModel.loadedState().error)

        repository.refreshError = null
        viewModel.onIntent(ProfessionalDetailIntent.Retry)

        assertNull(viewModel.loadedState().error)
        assertEquals(2, repository.refreshCalls)
    }

    @Test
    fun `offline, a professional cached from the list shows its summary`() = runTest {
        repository.details.value = aProfessionalDetails(aboutMe = null)
        repository.refreshError = AppError.NoConnection

        val state = viewModel().loadedState()

        assertEquals(aProfessional(), state.details?.professional)
        assertNull(state.details?.aboutMe)
        assertEquals(AppError.NoConnection, state.error)
    }
}