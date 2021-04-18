package com.pedrenrique.tecnonutri.data

import io.realm.DynamicRealm
import io.realm.RealmMigration
import javax.inject.Inject

const val DB_VERSION = 1L

class Migration @Inject constructor() : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        // if (oldVersion < 1L) upTo1(realm) // do migration
    }
}