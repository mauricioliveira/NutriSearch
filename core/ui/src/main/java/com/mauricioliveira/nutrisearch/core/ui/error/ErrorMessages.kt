package com.mauricioliveira.nutrisearch.core.ui.error

import androidx.annotation.StringRes
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.ui.R

@StringRes
fun AppError.messageRes(): Int = when (this) {
    AppError.NoConnection -> R.string.error_no_connection
    AppError.NotFound -> R.string.error_not_found
    is AppError.Server -> R.string.error_server
    is AppError.Unknown -> R.string.error_unknown
}

@StringRes
fun AppError.staleDataMessageRes(): Int = when (this) {
    AppError.NoConnection -> R.string.stale_data_offline
    AppError.NotFound, is AppError.Server, is AppError.Unknown -> R.string.stale_data_refresh_failed
}