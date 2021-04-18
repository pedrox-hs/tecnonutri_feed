package com.pedrenrique.tecnonutri.data.di

import android.content.Context
import com.pedrenrique.tecnonutri.data.DB_VERSION
import com.pedrenrique.tecnonutri.data.Migration
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration

@Module
abstract class StorageModule {

    companion object {

        @Provides
        fun provideRealmConfiguration(
            context: Context,
            migration: RealmMigration
        ): RealmConfiguration {
            Realm.init(context)

            return RealmConfiguration.Builder()
                .schemaVersion(DB_VERSION)
                .migration(migration)
                .build()
        }
    }

    @Binds
    abstract fun bindRealmMigration(migration: Migration): RealmMigration
}