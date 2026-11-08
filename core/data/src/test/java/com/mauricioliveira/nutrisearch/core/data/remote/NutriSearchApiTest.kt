package com.mauricioliveira.nutrisearch.core.data.remote

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class NutriSearchApiTest {

    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    private fun api(status: HttpStatusCode = HttpStatusCode.OK, body: String, onRequest: (String) -> Unit = {}) =
        NutriSearchApi(
            HttpClientFactory.create(
                engine = MockEngine { request ->
                    onRequest(request.url.toString())
                    respond(body, status, jsonHeaders)
                },
                baseUrl = "https://nutrisearch.test/",
            ),
        )

    @Test
    fun `search sends paging and sort params and parses the response`() = runTest {
        var url = ""
        val response = api(body = SEARCH_JSON, onRequest = { url = it })
            .searchProfessionals(limit = 4, offset = 8, sortBy = "rating")

        assertEquals("https://nutrisearch.test/professionals/search?limit=4&offset=8&sort_by=rating", url)
        assertEquals(10, response.count)
        assertEquals(1, response.professionals.size)
        with(response.professionals.single()) {
            assertEquals(4, id)
            assertEquals(5, rating)
            assertEquals(48, ratingCount)
            assertEquals(listOf("Spanish"), languages)
            assertNull(aboutMe)
        }
    }

    @Test
    fun `profile parses about_me`() = runTest {
        val dto = api(body = PROFILE_JSON).getProfessional(4)
        assertEquals("There is no word to describe me.", dto.aboutMe)
    }

    @Test
    fun `unknown profile throws on 404`() = runTest {
        assertFailsWith<ClientRequestException> {
            api(status = HttpStatusCode.NotFound, body = """{"error":"Professional not found"}""")
                .getProfessional(999)
        }
    }

    private companion object {
        const val ITEM = """
            "expertise": ["Weight Gain", "Diabetes Management", "Sports Nutrition"],
            "id": 4,
            "languages": ["Spanish"],
            "name": "Oliver Brown",
            "profile_picture_url": "https://nutrisearch.vercel.app/static/image-4.jpg",
            "rating": 5,
            "rating_count": 48
        """
        const val SEARCH_JSON = """{"count": 10, "limit": 4, "offset": 8, "professionals": [{ $ITEM }]}"""
        const val PROFILE_JSON = """{"about_me": "There is no word to describe me.", $ITEM}"""
    }
}
