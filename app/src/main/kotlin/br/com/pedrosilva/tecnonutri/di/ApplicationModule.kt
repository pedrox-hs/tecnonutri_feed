package br.com.pedrosilva.tecnonutri.di

import android.app.Application
import android.content.Context
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl
import com.pedrenrique.tecnonutri.data.di.DataModule
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [DataModule::class])
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun bindMainThread(mainThread: MainThreadImpl): MainThread
}