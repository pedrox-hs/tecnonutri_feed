package com.pedrenrique.tecnonutri.domain

import java.util.Date

data class FeedItem(
    val id: String,
    val date: Date,
    val profile: Profile,
    val imageUrl: String? = null,
    val meal: MealType? = null,
    val energy: Double? = null,
    val carbohydrate: Double? = null,
    val fat: Double? = null,
    val protein: Double? = null,
    val foods: List<Food> = listOf(),
    val isLiked: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val feedItem = other as FeedItem
        return id == feedItem.id
    }

    override fun hashCode() = id.hashCode()
}