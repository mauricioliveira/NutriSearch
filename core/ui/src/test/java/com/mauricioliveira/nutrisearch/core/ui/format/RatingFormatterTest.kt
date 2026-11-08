package com.mauricioliveira.nutrisearch.core.ui.format

import org.junit.Assert.assertEquals
import org.junit.Test

class RatingFormatterTest {
    @Test
    fun `formats rating with count`() {
        assertEquals("4 / 5 (30)", formatRating(rating = 4, ratingCount = 30))
    }
}
