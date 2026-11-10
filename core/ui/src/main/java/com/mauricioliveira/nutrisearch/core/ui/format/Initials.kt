package com.mauricioliveira.nutrisearch.core.ui.format

fun initialsOf(name: String): String {
    val words = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when (words.size) {
        0 -> ""
        1 -> words.first().take(1)
        else -> "${words.first().first()}${words.last().first()}"
    }.uppercase()
}