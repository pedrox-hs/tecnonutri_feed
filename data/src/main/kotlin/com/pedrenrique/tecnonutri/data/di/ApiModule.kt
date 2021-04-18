package com.pedrenrique.tecnonutri.data.di

import com.pedrenrique.tecnonutri.data.net.services.FeedService
import com.pedrenrique.tecnonutri.data.net.services.ProfileService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create

@Module(includes = [NetworkModule::class])
internal class ApiModule {

    @Provides
    fun provideFeedService(retrofit: Retrofit): FeedService = retrofit.create()

    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService = retrofit.create()
}