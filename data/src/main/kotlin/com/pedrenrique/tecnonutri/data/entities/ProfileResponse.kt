package com.pedrenrique.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

internal data class ProfileResponse(
    @SerializedName("p")
    val page: Int = 0,

    @SerializedName("profile")
    val profile: Profile,

    @SerializedName("items")
    val items: List<FeedItem> = listOf(),

    @SerializedName("t")
    val timestamp: Int = 0,

    @SerializedName("success")
    val isSuccess: Boolean = false
)