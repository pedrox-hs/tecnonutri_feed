package br.com.pedrosilva.tecnonutri.di

import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindMainThread(mainThread: MainThreadImpl): MainThread
}