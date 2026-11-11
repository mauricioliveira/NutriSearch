package com.mauricioliveira.nutrisearch.core.domain.model

sealed class AppError : Exception() {
    data object NoConnection : AppError()
    data object NotFound : AppError()
    data class Server(val code: Int) : AppError()
    data class Unknown(override val cause: Throwable? = null) : AppError()
}

/** For errors that arrive typed as Throwable (Paging LoadState, Result): keeps an AppError, wraps anything else. */
fun Throwable.asAppError(): AppError = this as? AppError ?: AppError.Unknown(this)