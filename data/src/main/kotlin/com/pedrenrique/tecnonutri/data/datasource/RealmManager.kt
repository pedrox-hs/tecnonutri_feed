package com.pedrenrique.tecnonutri.data.datasource

import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmManager @Inject constructor(configuration: RealmConfiguration) {
    init {
        Realm.setDefaultConfiguration(configuration)
    }

    fun <T> execute(fn: Realm.() -> T) =
        Realm.getDefaultInstance().use { realm ->
            realm.fn()
        }

    fun transactionAsync(fn: Realm.() -> Unit): Unit =
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransactionAsync {
                it.fn()
            }
        }
}