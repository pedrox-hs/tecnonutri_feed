package com.pedrenrique.tecnonutri.data.converters

import com.pedrenrique.tecnonutri.data.entities.MealType
import com.pedrenrique.tecnonutri.domain.MealType as MealTypeDTO

internal fun MealTypeDTO.asDataModel() = MealType.valueOf(name)

internal fun MealType.asDTO() = MealTypeDTO.valueOf(name)