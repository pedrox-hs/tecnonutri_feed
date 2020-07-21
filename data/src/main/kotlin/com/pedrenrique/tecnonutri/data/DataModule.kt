package com.pedrenrique.tecnonutri.data

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

object DataModule {
    fun init(context: Context) {
        Realm.init(context)
        val migration = Migration()
        val realmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(Migration.DB_VERSION)
            .migration(migration)
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}