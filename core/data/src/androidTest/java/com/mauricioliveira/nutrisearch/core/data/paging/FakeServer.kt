package com.mauricioliveira.nutrisearch.core.data.paging

import com.mauricioliveira.nutrisearch.core.data.remote.HttpClientFactory
import com.mauricioliveira.nutrisearch.core.data.remote.NutriSearchApi
import com.mauricioliveira.nutrisearch.core.data.remote.dto.ProfessionalDto
import com.mauricioliveira.nutrisearch.core.data.remote.dto.SearchResponseDto
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json
import java.io.IOException

/** Behaves like the real API: slices [all] by the limit/offset the client sends. */
internal class FakeServer(var all: List<ProfessionalDto>) {
    var offline = false
    val requests = mutableListOf<Url>()

    val api = NutriSearchApi(
        HttpClientFactory.create(
            engine = MockEngine { request ->
                requests += request.url
                if (offline) throw IOException("offline")
                val limit = request.url.parameters["limit"]!!.toInt()
                val offset = request.url.parameters["offset"]!!.toInt()
                val page = SearchResponseDto(
                    count = all.size,
                    limit = limit,
                    offset = offset,
                    professionals = all.drop(offset).take(limit),
                )
                respond(
                    content = Json.encodeToString(page),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
            baseUrl = "https://nutrisearch.test/",
        ),
    )
}

internal fun aDto(id: Int) = ProfessionalDto(
    id = id,
    name = "Professional $id",
    rating = 4,
    ratingCount = 10 * id,
    languages = listOf("English", "Portuguese"),
    expertise = listOf("Weight Loss", "Nutrition"),
    profilePictureUrl = null,
)