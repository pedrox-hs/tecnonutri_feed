package br.com.pedrosilva.tecnonutri

import android.app.Application
import br.com.pedrosilva.tecnonutri.util.ContextHelper
import com.crashlytics.android.Crashlytics
import com.pedrenrique.tecnonutri.data.DataModule
import io.fabric.sdk.android.Fabric

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextHelper.init(applicationContext)
        Fabric.with(this, Crashlytics())
        initData()
    }

    private fun initData() {
        DataModule.init(this)
    }
}