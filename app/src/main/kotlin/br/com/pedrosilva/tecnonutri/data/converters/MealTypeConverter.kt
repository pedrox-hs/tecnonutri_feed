package br.com.pedrosilva.tecnonutri.data.converters

import br.com.pedrosilva.tecnonutri.data.entities.MealType
import br.com.pedrosilva.tecnonutri.domain.entities.MealType as MealTypeDTO

internal fun MealTypeDTO.asDataModel() = MealType.valueOf(name)

internal fun MealType.asDTO() = MealTypeDTO.valueOf(name)