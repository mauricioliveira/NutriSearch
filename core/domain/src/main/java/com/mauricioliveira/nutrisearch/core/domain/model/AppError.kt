package com.mauricioliveira.nutrisearch.core.domain.model

sealed class AppError : Exception() {
    data object NoConnection : AppError()
    data object NotFound : AppError()
    data class Server(val code: Int) : AppError()
    data class Unknown(override val cause: Throwable? = null) : AppError()
}
