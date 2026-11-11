package com.mauricioliveira.nutrisearch.core.testing

import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails

fun aProfessional(
    id: Int = 4,
    name: String = "Oliver Brown",
    rating: Int = 5,
    ratingCount: Int = 48,
    languages: List<String> = listOf("Spanish"),
    expertise: List<String> = listOf("Weight Gain", "Diabetes Management", "Sports Nutrition"),
    profilePictureUrl: String? = "https://nutrisearch.vercel.app/static/image-4.jpg",
) = Professional(id, name, rating, ratingCount, languages, expertise, profilePictureUrl)

fun aProfessionalDetails(
    professional: Professional = aProfessional(),
    aboutMe: String? = "There is no word to describe me.",
) = ProfessionalDetails(professional, aboutMe)
