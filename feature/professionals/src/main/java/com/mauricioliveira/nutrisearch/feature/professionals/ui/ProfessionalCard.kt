package com.mauricioliveira.nutrisearch.feature.professionals.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.ui.component.ProfessionalSummary
import com.mauricioliveira.nutrisearch.core.ui.theme.NutriSearchTheme

@Composable
internal fun ProfessionalCard(
    professional: Professional,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        ProfessionalSummary(
            professional = professional,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfessionalCardPreview() {
    NutriSearchTheme {
        ProfessionalCard(
            professional = Professional(
                id = 1,
                name = "Dr. Jane Doe",
                rating = 4,
                ratingCount = 42,
                languages = listOf("English", "Spanish"),
                expertise = listOf("Weight Loss", "Nutrition"),
                profilePictureUrl = null,
            ),
            onClick = {},
        )
    }
}

