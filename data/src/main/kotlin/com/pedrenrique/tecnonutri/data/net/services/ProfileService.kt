package com.pedrenrique.tecnonutri.data.net.services

import com.pedrenrique.tecnonutri.data.entities.ProfileResponse
import com.pedrenrique.tecnonutri.data.net.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProfileService {

    @GET(ApiConstants.PROFILE)
    fun profile(@Path("id") id: Int): Call<ProfileResponse>

    @GET(ApiConstants.PROFILE)
    fun posts(
        @Path("id") id: Int,
        @Query("p") page: Int,
        @Query("t") timestamp: Int
    ): Call<ProfileResponse>
}