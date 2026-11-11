package com.mauricioliveira.nutrisearch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mauricioliveira.nutrisearch.feature.professionaldetail.navigation.navigateToProfessionalDetail
import com.mauricioliveira.nutrisearch.feature.professionaldetail.navigation.professionalDetailScreen
import com.mauricioliveira.nutrisearch.feature.professionals.navigation.ProfessionalsDestination
import com.mauricioliveira.nutrisearch.feature.professionals.navigation.professionalsScreen

@Composable
fun NutriSearchNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ProfessionalsDestination) {
        professionalsScreen(onProfessionalClick = navController::navigateToProfessionalDetail)
        professionalDetailScreen(onBack = navController::navigateUp)
    }
}
