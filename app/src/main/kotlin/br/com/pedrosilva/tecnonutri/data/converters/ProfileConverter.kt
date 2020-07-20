package br.com.pedrosilva.tecnonutri.data.converters

import br.com.pedrosilva.tecnonutri.data.entities.Profile
import br.com.pedrosilva.tecnonutri.domain.entities.Profile as ProfileDTO

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