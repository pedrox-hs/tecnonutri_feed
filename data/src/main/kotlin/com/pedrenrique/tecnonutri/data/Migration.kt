package com.pedrenrique.tecnonutri.data

import io.realm.DynamicRealm
import io.realm.RealmMigration

class Migration : RealmMigration {

    companion object {
        const val DB_VERSION = 1L
    }

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        // if (oldVersion < 1L) upTo1(realm) // do migration
    }
}