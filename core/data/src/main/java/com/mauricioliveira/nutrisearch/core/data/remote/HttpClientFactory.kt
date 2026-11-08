package com.mauricioliveira.nutrisearch.core.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {

    fun create(engine: HttpClientEngine, baseUrl: String, enableLogging: Boolean = false): HttpClient =
        HttpClient(engine) {
            expectSuccess = true
            defaultRequest { url(baseUrl) }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
            }
            if (enableLogging) {
                install(Logging) { level = LogLevel.INFO }
            }
        }
}
