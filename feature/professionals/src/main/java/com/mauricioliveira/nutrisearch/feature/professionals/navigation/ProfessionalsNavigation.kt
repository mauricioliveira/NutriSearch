package com.mauricioliveira.nutrisearch.feature.professionals.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mauricioliveira.nutrisearch.feature.professionals.ui.ProfessionalsRoute
import kotlinx.serialization.Serializable

@Serializable
data object ProfessionalsDestination

fun NavGraphBuilder.professionalsScreen(onProfessionalClick: (Int) -> Unit) {
    composable<ProfessionalsDestination> {
        ProfessionalsRoute(onProfessionalClick = onProfessionalClick)
    }
}
