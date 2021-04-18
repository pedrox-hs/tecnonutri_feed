package com.pedrenrique.tecnonutri.data.di

import com.pedrenrique.tecnonutri.data.repositories.FeedRepositoryImpl
import com.pedrenrique.tecnonutri.data.repositories.ProfileRepositoryImpl
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import dagger.Binds
import dagger.Module

@Module(includes = [StorageModule::class, ApiModule::class])
abstract class DataModule {

    @Binds
    internal abstract fun bindFeedRepository(repository: FeedRepositoryImpl): FeedRepository

    @Binds
    internal abstract fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository
}