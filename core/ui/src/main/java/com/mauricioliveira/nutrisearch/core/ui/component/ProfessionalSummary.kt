package com.mauricioliveira.nutrisearch.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.ui.format.formatRating
import com.mauricioliveira.nutrisearch.core.ui.theme.NutriSearchTheme

@Composable
fun ProfessionalSummary(
    professional: Professional,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 96.dp,
    nameStyle: TextStyle = MaterialTheme.typography.titleLarge,
    avatarTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar(
                name = professional.name,
                imageUrl = professional.profilePictureUrl,
                modifier = Modifier.size(avatarSize),
                textStyle = avatarTextStyle,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = professional.name, style = nameStyle)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(rating = professional.rating)
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = formatRating(professional.rating, professional.ratingCount),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = professional.languages.joinToString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            professional.expertise.forEach { Tag(text = it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfessionalSummaryPreview() {
    NutriSearchTheme {
        ProfessionalSummary(
            professional = Professional(
                id = 1,
                name = "Dr. Jane Doe",
                rating = 4,
                ratingCount = 42,
                languages = listOf("English", "Spanish"),
                expertise = listOf("Weight Loss", "Nutrition"),
                profilePictureUrl = null,
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}


