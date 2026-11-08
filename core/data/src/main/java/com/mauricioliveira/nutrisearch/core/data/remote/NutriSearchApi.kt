package com.mauricioliveira.nutrisearch.core.data.remote

import com.mauricioliveira.nutrisearch.core.data.remote.dto.ProfessionalDto
import com.mauricioliveira.nutrisearch.core.data.remote.dto.SearchResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class NutriSearchApi(private val client: HttpClient) {

    suspend fun searchProfessionals(limit: Int, offset: Int, sortBy: String): SearchResponseDto =
        client.get("professionals/search") {
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("sort_by", sortBy)
        }.body()

    suspend fun getProfessional(id: Int): ProfessionalDto =
        client.get("professionals/$id").body()
}
