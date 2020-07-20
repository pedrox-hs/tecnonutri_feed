package br.com.pedrosilva.tecnonutri.data.entities

import com.google.gson.annotations.SerializedName

class FeedItemResponse(
    @SerializedName("item")
    val item: FeedItem,

    @SerializedName("success")
    val isSuccess: Boolean = false
)