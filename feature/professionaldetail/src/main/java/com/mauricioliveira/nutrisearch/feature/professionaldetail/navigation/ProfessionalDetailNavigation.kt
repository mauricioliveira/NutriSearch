package com.mauricioliveira.nutrisearch.feature.professionaldetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mauricioliveira.nutrisearch.feature.professionaldetail.ui.ProfessionalDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data class ProfessionalDetailDestination(val professionalId: Int)

fun NavController.navigateToProfessionalDetail(professionalId: Int) =
    navigate(ProfessionalDetailDestination(professionalId))

fun NavGraphBuilder.professionalDetailScreen(onBack: () -> Unit) {
    composable<ProfessionalDetailDestination> { entry ->
        ProfessionalDetailRoute(
            professionalId = entry.toRoute<ProfessionalDetailDestination>().professionalId,
            onBack = onBack,
        )
    }
}