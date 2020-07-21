package com.pedrenrique.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

internal data class FeedItemResponse(
    @SerializedName("item")
    val item: FeedItem,

    @SerializedName("success")
    val isSuccess: Boolean = false
)