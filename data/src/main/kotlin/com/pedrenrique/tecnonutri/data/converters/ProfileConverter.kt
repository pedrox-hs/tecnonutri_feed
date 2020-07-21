package com.pedrenrique.tecnonutri.data.converters

import com.pedrenrique.tecnonutri.data.entities.Profile
import com.pedrenrique.tecnonutri.domain.Profile as ProfileDTO

internal fun ProfileDTO.asDataModel() =
    Profile(
        id = id,
        name = name,
        imageUrl = imageUrl,
        generalGoal = generalGoal
    )

internal fun Profile.asDTO() =
    ProfileDTO(
        id = id,
        name = name,
        imageUrl = imageUrl,
        generalGoal = generalGoal
    )