package com.mauricioliveira.nutrisearch.core.data.mapper

import com.mauricioliveira.nutrisearch.core.domain.model.AppError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException


internal fun Throwable.toAppError(): AppError = when(this) {
    is AppError -> this
    is ClientRequestException -> {
        if (response.status == HttpStatusCode.NotFound) {
            AppError.NotFound
        } else {
            AppError.Server(response.status.value)
        }
    }

    is ServerResponseException -> AppError.Server(response.status.value)
    is IOException -> AppError.NoConnection // no network, DNS failure, timeouts
    else -> AppError.Unknown(this)
}