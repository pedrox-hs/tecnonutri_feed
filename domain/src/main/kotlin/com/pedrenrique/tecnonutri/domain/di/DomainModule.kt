package com.pedrenrique.tecnonutri.domain.di

import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.impl.ThreadExecutor
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor
import com.pedrenrique.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetFeedInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetFeedItemInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetProfileInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindExecutor(executor: ThreadExecutor): Executor

    @Binds
    fun bindChangeLikeInteractor(interactor: ChangeLikeInteractorImpl): ChangeLikeInteractor

    @Binds
    fun bindGetFeedInteractor(interactor: GetFeedInteractorImpl): GetFeedInteractor

    @Binds
    fun bindGetFeedItemInteractor(interactor: GetFeedItemInteractorImpl): GetFeedItemInteractor

    @Binds
    fun bindGetProfileInteractor(interactor: GetProfileInteractorImpl): GetProfileInteractor
}