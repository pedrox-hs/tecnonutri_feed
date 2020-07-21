package com.pedrenrique.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

internal data class Food (
    @SerializedName("description")
    val description: String? = null,

    @SerializedName("measure")
    val measure: String? = null,

    @SerializedName("amount")
    val amount: Double? = null,

    @SerializedName("weight")
    val weight: Double? = null,

    @SerializedName("energy")
    val energy: Double? = null,

    @SerializedName("carbohydrate")
    val carbohydrate: Double? = null,

    @SerializedName("fat")
    val fat: Double? = null,

    @SerializedName("protein")
    val protein: Double? = null
)