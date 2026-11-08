package com.mauricioliveira.nutrisearch.core.ui.format

/** Formats a rating as required by the brief, e.g. "4 / 5 (30)". */
fun formatRating(rating: Int, ratingCount: Int): String = "$rating / 5 ($ratingCount)"
