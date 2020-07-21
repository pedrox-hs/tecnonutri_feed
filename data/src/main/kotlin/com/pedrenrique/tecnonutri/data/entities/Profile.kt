package com.pedrenrique.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

internal data class Profile(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("image")
    val imageUrl: String? = null,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("general_goal")
    val generalGoal: String = ""
)