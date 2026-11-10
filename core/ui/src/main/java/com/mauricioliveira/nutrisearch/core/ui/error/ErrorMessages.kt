package com.mauricioliveira.nutrisearch.core.ui.error

import androidx.annotation.StringRes
import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import com.mauricioliveira.nutrisearch.core.ui.R

@StringRes
fun Throwable.messageRes(): Int = when(this) {
    AppError.NoConnection -> R.string.error_no_connection
    AppError.NotFound -> R.string.error_not_found
    is AppError.Server -> R.string.error_server
    else -> R.string.error_unknown
}