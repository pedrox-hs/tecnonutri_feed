package br.com.pedrosilva.tecnonutri.data.net.services

import br.com.pedrosilva.tecnonutri.data.entities.FeedItemResponse
import br.com.pedrosilva.tecnonutri.data.entities.FeedResponse
import br.com.pedrosilva.tecnonutri.data.net.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedService {

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