package br.com.pedrosilva.tecnonutri

import android.app.Application
import com.pedrenrique.tecnonutri.data.DataModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    private fun initData() {
        DataModule.init(this)
    }
}