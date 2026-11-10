package com.mauricioliveira.nutrisearch.core.ui.format

const val MAX_RATING = 5

/** Formats a rating as required by the brief, e.g. "4 / 5 (30)". */
fun formatRating(rating: Int, ratingCount: Int): String = "$rating / $MAX_RATING ($ratingCount)"
