package br.com.pedrosilva.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("p")
    val page: Int = 0,

    @SerializedName("items")
    val items: List<FeedItem> = listOf(),

    @SerializedName("t")
    val timestamp: Int = 0,

    @SerializedName("success")
    val isSuccess: Boolean = false
)