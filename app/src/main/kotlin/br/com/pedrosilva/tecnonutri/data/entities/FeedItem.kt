package br.com.pedrosilva.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName
import java.util.Date

data class FeedItem (
    @SerializedName("feedHash")
    val feedHash: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("profile")
    val profile: Profile,

    @SerializedName("image")
    val imageUrl: String? = null,

    @SerializedName("meal")
    val meal: MealType? = null,

    @SerializedName("energy")
    val energy: Double? = null,

    @SerializedName("carbohydrate")
    val carbohydrate: Double? = null,

    @SerializedName("fat")
    val fat: Double? = null,

    @SerializedName("protein")
    val protein: Double? = null,

    @SerializedName("foods")
    val foods: List<Food>? = listOf(),

    @SerializedName("liked")
    val isLiked: Boolean = false
)