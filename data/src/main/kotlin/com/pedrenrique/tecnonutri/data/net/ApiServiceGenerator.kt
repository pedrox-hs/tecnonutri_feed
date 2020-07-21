package com.pedrenrique.tecnonutri.data.net

import com.pedrenrique.tecnonutri.data.net.json.GsonUTCDateAdapter
import com.google.gson.GsonBuilder
import com.pedrenrique.tecnonutri.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

internal object ApiServiceGenerator {

    private val builder = Retrofit.Builder()
    private val httpClient = OkHttpClient.Builder()

    private fun <T> getService(serviceClass: Class<T>): T = builder.build().create(serviceClass)

    inline fun <reified T> getService(): T = getService(T::class.java)

    init {
        val gson = GsonBuilder()
            // http://stackoverflow.com/questions/26044881/java-date-to-utc-using-gson
            .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
            .create()
        if (BuildConfig.DEBUG) {
            // enable logging
            val loggingInterceptor = HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
            httpClient.addInterceptor(loggingInterceptor)
        }
        builder.baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }
}