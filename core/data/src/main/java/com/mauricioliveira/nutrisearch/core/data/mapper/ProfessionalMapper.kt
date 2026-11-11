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
    aboutMe = aboutMe.orEmpty(),
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

/** Any cached row is a profile; [ProfessionalDetails.aboutMe] stays null until the profile endpoint answers. */
internal fun ProfessionalEntity.toDetails() = ProfessionalDetails(
    professional = toDomain(),
    aboutMe = aboutMe,
)
