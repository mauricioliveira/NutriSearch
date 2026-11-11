package com.mauricioliveira.nutrisearch.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.ui.R
import com.mauricioliveira.nutrisearch.core.ui.error.staleDataMessageRes
import com.mauricioliveira.nutrisearch.core.ui.theme.NutriSearchTheme

/** Shown over cached data when the last refresh failed. */
@Composable
fun StaleDataBanner(
    error: AppError,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (error == AppError.NoConnection) Icons.Default.CloudOff else Icons.Default.ErrorOutline,
                contentDescription = null,
            )
            Text(
                text = stringResource(error.staleDataMessageRes()),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StaleDataBannerOfflinePreview() {
    NutriSearchTheme {
        StaleDataBanner(error = AppError.NoConnection, modifier = Modifier.padding(16.dp), onRetry = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun StaleDataBannerServerErrorPreview() {
    NutriSearchTheme {
        StaleDataBanner(error = AppError.Server(500), modifier = Modifier.padding(16.dp), onRetry = {})
    }
}

