package br.com.pedrosilva.tecnonutri;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import br.com.pedrosilva.tecnonutri.util.ContextHelper;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ContextHelper.init(getApplicationContext());

        Fabric.with(this, new Crashlytics());
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
