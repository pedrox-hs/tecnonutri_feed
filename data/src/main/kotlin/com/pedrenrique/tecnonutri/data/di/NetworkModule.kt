package com.pedrenrique.tecnonutri.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pedrenrique.tecnonutri.data.BuildConfig
import com.pedrenrique.tecnonutri.data.net.ApiConstants.BASE_URL
import com.pedrenrique.tecnonutri.data.net.json.GsonUTCDateAdapter
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
    @Singleton
    @JvmSuppressWildcards
    fun provideRetrofit(client: OkHttpClient, converters: Set<Converter.Factory>): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .apply {
                converters.forEach { addConverterFactory(it) }
            }
            .client(client)
            .build()

    @Provides
    @JvmSuppressWildcards
    fun provideOkHttpClient(interceptors: Set<Interceptor>): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                interceptors.forEach { addInterceptor(it) }
            }
            .build()

    @Provides
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .apply {
                level = if (!BuildConfig.DEBUG) Level.NONE else Level.BODY
            }

    @Provides
    @IntoSet
    fun provideGsonConverterFactory(gson: Gson): Converter.Factory =
        GsonConverterFactory.create(gson)

    @Provides
    fun provideGson(utcDateAdapter: GsonUTCDateAdapter): Gson =
        GsonBuilder()
            // http://stackoverflow.com/questions/26044881/java-date-to-utc-using-gson
            .registerTypeAdapter(Date::class.java, utcDateAdapter)
            .create()
}