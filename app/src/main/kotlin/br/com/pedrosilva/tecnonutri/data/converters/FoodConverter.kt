package br.com.pedrosilva.tecnonutri.data.converters

import br.com.pedrosilva.tecnonutri.data.entities.Food
import br.com.pedrosilva.tecnonutri.domain.entities.Food as FoodDTO

internal fun FoodDTO.asDataModel() = Food(
    description = description,
    measure = measure,
    amount = amount,
    weight = weight,
    carbohydrate = carbohydrate,
    energy = energy,
    fat = fat,
    protein = protein
)

internal fun Food.asDTO() = FoodDTO(
    description = description,
    measure = measure,
    amount = amount,
    weight = weight,
    carbohydrate = carbohydrate,
    energy = energy,
    fat = fat,
    protein = protein
)

internal fun List<FoodDTO>.asDataModel() = map { it.asDataModel() }

internal fun List<Food>.asDTO() = map { it.asDTO() }