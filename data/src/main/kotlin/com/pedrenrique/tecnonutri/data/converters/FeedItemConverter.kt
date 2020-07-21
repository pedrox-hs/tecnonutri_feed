package com.pedrenrique.tecnonutri.data.converters

import com.pedrenrique.tecnonutri.data.entities.FeedItem
import com.pedrenrique.tecnonutri.domain.FeedItem as FeedItemDTO

internal fun FeedItemDTO.asDataModel(): FeedItem = run {
    FeedItem(
        feedHash = id,
        imageUrl = imageUrl,
        profile = profile.asDataModel(),
        date = date,
        meal = meal?.asDataModel(),
        isLiked = isLiked,
        carbohydrate = carbohydrate,
        energy = energy,
        fat = fat,
        protein = protein,
        foods = foods.asDataModel()
    )
}

internal fun FeedItem.asDTO() = run {
    FeedItemDTO(
        id = feedHash,
        imageUrl = imageUrl,
        profile = profile.asDTO(),
        date = date,
        meal = meal?.asDTO(),
        isLiked = isLiked,
        carbohydrate = carbohydrate,
        energy = energy,
        fat = fat,
        protein = protein,
        foods = foods?.asDTO() ?: listOf()
    )
}

internal fun List<FeedItemDTO>.asDataModel() = map { it.asDataModel() }

internal fun List<FeedItem>.asDTO() = map { it.asDTO() }