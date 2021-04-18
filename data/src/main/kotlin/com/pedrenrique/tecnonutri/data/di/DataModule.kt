package com.pedrenrique.tecnonutri.data.di

import com.pedrenrique.tecnonutri.data.repositories.FeedRepositoryImpl
import com.pedrenrique.tecnonutri.data.repositories.ProfileRepositoryImpl
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindFeedRepository(repository: FeedRepositoryImpl): FeedRepository

    @Binds
    fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository
}