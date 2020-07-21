package com.pedrenrique.tecnonutri.data.net.services

import com.pedrenrique.tecnonutri.data.entities.FeedItemResponse
import com.pedrenrique.tecnonutri.data.entities.FeedResponse
import com.pedrenrique.tecnonutri.data.net.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface FeedService {

    @GET(ApiConstants.FEED)
    fun list(): Call<FeedResponse>

    @GET(ApiConstants.FEED)
    fun paginate(
        @Query("p") page: Int,
        @Query("t") timestamp: Int
    ): Call<FeedResponse>

    @GET(ApiConstants.FEED_ITEM)
    fun item(@Path("feedHash") feedHash: String): Call<FeedItemResponse>
}