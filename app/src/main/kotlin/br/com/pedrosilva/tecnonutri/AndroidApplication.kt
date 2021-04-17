package br.com.pedrosilva.tecnonutri

import android.app.Application
import br.com.pedrosilva.tecnonutri.util.ContextHelper
import com.pedrenrique.tecnonutri.data.DataModule

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextHelper.init(applicationContext)
        initData()
    }

    private fun initData() {
        DataModule.init(this)
    }
}