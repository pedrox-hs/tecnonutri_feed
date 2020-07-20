package br.com.pedrosilva.tecnonutri

import android.app.Application
import br.com.pedrosilva.tecnonutri.util.ContextHelper
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextHelper.init(applicationContext)
        Fabric.with(this, Crashlytics())
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}