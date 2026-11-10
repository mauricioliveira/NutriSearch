package com.mauricioliveira.nutrisearch.core.data.mapper

import com.mauricioliveira.nutrisearch.core.data.remote.dto.ProfessionalDto
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalEntity
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalSummaryEntity
import com.mauricioliveira.nutrisearch.core.domain.model.Professional
import com.mauricioliveira.nutrisearch.core.domain.model.ProfessionalDetails

internal fun ProfessionalDto.toEntity() = ProfessionalEntity(
    id = id,
    name = name,
    rating = rating,
    ratingCount = ratingCount,
    languages = languages,
    expertise = expertise,
    profilePictureUrl = profilePictureUrl,
    aboutMe = aboutMe,
)

internal fun ProfessionalDto.toSummaryEntity() = ProfessionalSummaryEntity(
    id = id,
    name = name,
    rating = rating,
    ratingCount = ratingCount,
    languages = languages,
    expertise = expertise,
    profilePictureUrl = profilePictureUrl,
)

internal fun ProfessionalEntity.toDomain() = Professional(
    id = id,
    name = name,
    rating = rating,
    ratingCount = ratingCount,
    languages = languages,
    expertise = expertise,
    profilePictureUrl = profilePictureUrl,
)

/** Null while only the list summary is cached (no about_me yet). */
internal fun ProfessionalEntity.toDetailsOrNull(): ProfessionalDetails? =
    aboutMe?.let { ProfessionalDetails(professional = toDomain(), aboutMe = it) }
