package br.com.pedrosilva.tecnonutri.data.net.services

import br.com.pedrosilva.tecnonutri.data.entities.ProfileResponse
import br.com.pedrosilva.tecnonutri.data.net.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET(ApiConstants.PROFILE)
    fun profile(@Path("id") id: Int): Call<ProfileResponse>

    @GET(ApiConstants.PROFILE)
    fun posts(
        @Path("id") id: Int,
        @Query("p") page: Int,
        @Query("t") timestamp: Int
    ): Call<ProfileResponse>
}