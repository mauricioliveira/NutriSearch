package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.testing.FakeProfessionalRepository
import com.mauricioliveira.nutrisearch.core.testing.MainDispatcherRule
import com.mauricioliveira.nutrisearch.core.testing.aProfessionalDetails
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProfessionalDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeProfessionalRepository()

    private fun viewModel() = ProfessionalDetailViewModel(
        professionalId = 4,
        repository = repository,
    )

    /** First state that is not waiting on anything: an error, or content that is not refreshing. */
    private suspend fun ProfessionalDetailViewModel.settledState() = state.first {
        when (it) {
            ProfessionalDetailState.Loading -> false
            is ProfessionalDetailState.Error -> true
            is ProfessionalDetailState.Content -> !it.isRefreshing
        }
    }

    @Test
    fun `shows the cached profile and refreshes it on open`() = runTest {
        repository.details.value = aProfessionalDetails()

        assertEquals(
            ProfessionalDetailState.Content(
                details = aProfessionalDetails(),
                isRefreshing = false,
                refreshError = null,
            ),
            viewModel().settledState(),
        )
        assertEquals(1, repository.refreshCalls)
    }

    @Test
    fun `a failed refresh keeps the cached profile and exposes the error`() = runTest {
        repository.details.value = aProfessionalDetails()
        repository.refreshError = AppError.NoConnection

        assertEquals(
            ProfessionalDetailState.Content(
                details = aProfessionalDetails(),
                isRefreshing = false,
                refreshError = AppError.NoConnection,
            ),
            viewModel().settledState(),
        )
    }

    @Test
    fun `nothing cached and a failed refresh shows a full screen error`() = runTest {
        repository.refreshError = AppError.NotFound

        assertEquals(ProfessionalDetailState.Error(AppError.NotFound), viewModel().settledState())
    }

    @Test
    fun `retry refreshes again and shows the profile`() = runTest {
        repository.refreshError = AppError.NoConnection
        val viewModel = viewModel()
        assertEquals(ProfessionalDetailState.Error(AppError.NoConnection), viewModel.settledState())

        repository.refreshError = null
        repository.details.value = aProfessionalDetails() // the real repository writes Room on success
        viewModel.onIntent(ProfessionalDetailIntent.Retry)

        assertEquals(
            ProfessionalDetailState.Content(
                details = aProfessionalDetails(),
                isRefreshing = false,
                refreshError = null,
            ),
            viewModel.settledState(),
        )
        assertEquals(2, repository.refreshCalls)
    }

    @Test
    fun `offline, a professional cached from the list shows its summary`() = runTest {
        repository.details.value = aProfessionalDetails(aboutMe = null)
        repository.refreshError = AppError.NoConnection

        assertEquals(
            ProfessionalDetailState.Content(
                details = aProfessionalDetails(aboutMe = null),
                isRefreshing = false,
                refreshError = AppError.NoConnection,
            ),
            viewModel().settledState(),
        )
    }

    @Test
    fun `retry while a refresh is running does not start a second one`() = runTest {
        val gate = CompletableDeferred<Unit>()
        repository.refreshGate = gate
        repository.details.value = aProfessionalDetails()

        val viewModel = viewModel()
        viewModel.onIntent(ProfessionalDetailIntent.Retry)

        assertEquals(1, repository.refreshCalls)

        gate.complete(Unit)
        assertEquals(
            ProfessionalDetailState.Content(
                details = aProfessionalDetails(),
                isRefreshing = false,
                refreshError = null,
            ),
            viewModel.settledState(),
        )
    }

    @Test
    fun `refreshing over a cached profile clears the previous error`() = runTest {
        repository.details.value = aProfessionalDetails()
        repository.refreshError = AppError.NoConnection
        val viewModel = viewModel()
        assertEquals(AppError.NoConnection, (viewModel.settledState() as ProfessionalDetailState.Content).refreshError)

        repository.refreshError = null
        viewModel.onIntent(ProfessionalDetailIntent.Retry)

        assertEquals(
            ProfessionalDetailState.Content(
                details = aProfessionalDetails(),
                isRefreshing = false,
                refreshError = null,
            ),
            viewModel.settledState(),
        )
        assertEquals(2, repository.refreshCalls)
    }
}