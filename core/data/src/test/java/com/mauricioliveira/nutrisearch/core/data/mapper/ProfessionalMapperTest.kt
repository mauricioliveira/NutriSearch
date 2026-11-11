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
    fun `a row cached from the list is a profile without about_me`() {
        val summaryOnly = dto.toEntity().copy(aboutMe = null)
        assertNull(summaryOnly.toDetails().aboutMe)
        assertEquals("Hi", dto.copy(aboutMe = "Hi").toEntity().toDetails().aboutMe)
    }

    @Test
    fun `sort options map to api values`() {
        assertEquals(
            listOf("best_match", "rating", "most_popular"),
            ProfessionalSort.entries.map { it.apiValue },
        )
    }

    @Test
    fun `a profile without about_me is stored as empty, not as not loaded`() {
        val entity = dto.toEntity()
        assertEquals("", entity.aboutMe)
    }
}
