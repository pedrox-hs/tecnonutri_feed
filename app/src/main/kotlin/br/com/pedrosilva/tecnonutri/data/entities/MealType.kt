package br.com.pedrosilva.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

enum class MealType {
    @SerializedName("0")
    BREAKFAST,
    @SerializedName("1")
    MORNING_SNACK,
    @SerializedName("2")
    LUNCH,
    @SerializedName("3")
    AFTERNOON_SNACK,
    @SerializedName("4")
    DINNER,
    @SerializedName("5")
    SUPPER,
    @SerializedName("6")
    PRE_WORKOUT,
    @SerializedName("7")
    POST_WORKOUT
}