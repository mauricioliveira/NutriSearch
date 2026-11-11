package com.mauricioliveira.nutrisearch.feature.professionaldetail.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails
import com.mauricioliveira.nutrisearch.core.ui.component.ErrorView
import com.mauricioliveira.nutrisearch.core.ui.component.LoadingView
import com.mauricioliveira.nutrisearch.core.ui.component.OfflineBanner
import com.mauricioliveira.nutrisearch.core.ui.component.ProfessionalSummary
import com.mauricioliveira.nutrisearch.core.ui.error.messageRes
import com.mauricioliveira.nutrisearch.core.ui.theme.NutriSearchTheme
import com.mauricioliveira.nutrisearch.feature.professionaldetail.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfessionalDetailScreen(
    state: ProfessionalDetailState,
    onIntent: (ProfessionalDetailIntent) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.professional_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when(val content = state.toDetailContent()) {
                DetailContent.Loading -> LoadingView(modifier = Modifier.fillMaxSize())
                is DetailContent.Error -> ErrorView(
                    message = stringResource(content.error.messageRes()),
                    onRetry = { onIntent(ProfessionalDetailIntent.Retry) },
                    modifier = Modifier.fillMaxSize(),
                )
                is DetailContent.Content -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                    ) {
                        if (content.isRefreshing) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                    if (content.showOfflineBanner) OfflineBanner()
                    ProfessionalProfile(details = content.details, refreshFailed = content.showOfflineBanner)
                }
            }
        }
    }
}

@Composable
private fun ProfessionalProfile(
    details: ProfessionalDetails,
    refreshFailed: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfessionalSummary(
            professional = details.professional,
            avatarSize = 120.dp,
            nameStyle = MaterialTheme.typography.headlineSmall,
            avatarTextStyle = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.about_me),
            style = MaterialTheme.typography.titleMedium,
        )
        AnimatedContent(
            targetState = details.aboutMe,
            contentKey = { aboutMe -> aboutMe != null },
            transitionSpec = {
                (fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 100)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 100)))
                    .using(SizeTransform(clip = false) { _, _ -> tween(durationMillis = 300) })
            },
            modifier = Modifier.fillMaxWidth(),
            label = "aboutMe",
        ) { aboutMe ->
            when {
                aboutMe != null -> ExpandableText(text = aboutMe, modifier = Modifier.fillMaxWidth())
                refreshFailed -> Text(
                    text = stringResource(R.string.about_me_unavailable),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                else -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 4,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var hasOverflow by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.animateContentSize(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                if (!expanded) hasOverflow = result.hasVisualOverflow
            },
        )

        if (hasOverflow || expanded) {
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(10.dp),
            ) {
                Text(text = stringResource(if (expanded) R.string.show_less else R.string.show_more))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(18.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfessionalDetailScreenPreview() {
    NutriSearchTheme {
        ProfessionalDetailScreen(
            state = ProfessionalDetailState(
                details = ProfessionalDetails(
                    professional = Professional(
                        id = 1,
                        name = "Dr. Jane Doe",
                        rating = 5,
                        ratingCount = 42,
                        languages = listOf("English", "Spanish"),
                        expertise = listOf("Weight Loss", "Sports Nutrition"),
                        profilePictureUrl = null,
                    ),
                    aboutMe = "Nutritionist with ten years of experience. ".repeat(10),
                ),
                isRefreshing = false,
                error = null,
            ),
            onIntent = {},
            onBack = {},
        )
    }
}

