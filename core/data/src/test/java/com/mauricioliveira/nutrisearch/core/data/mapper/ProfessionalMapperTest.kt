package com.mauricioliveira.nutrisearch.core.data.mapper

import com.mauricioliveira.nutrisearch.core.data.remote.dto.ProfessionalDto
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalSort
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ProfessionalMapperTest {

    private val dto = ProfessionalDto(
        id = 1, name = "Ana", rating = 4, ratingCount = 30,
        languages = listOf("Portuguese"), expertise = listOf("Pediatrics"),
        profilePictureUrl = "", aboutMe = null,
    )

    @Test
    fun `blank picture url is stored as null`() {
        assertNull(dto.toEntity().profilePictureUrl)
    }

    @Test
    fun `details are only available once about_me is cached`() {
        assertNull(dto.toEntity().toDetailsOrNull())
        assertNotNull(dto.copy(aboutMe = "Hi").toEntity().toDetailsOrNull())
    }

    @Test
    fun `sort options map to api values`() {
        assertEquals(
            listOf("best_match", "rating", "most_popular"),
            ProfessionalSort.entries.map { it.apiValue },
        )
    }
}
