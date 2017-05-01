package krypt.com.krypt;

import android.app.Application;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import krypt.com.krypt.utils.PrimaryKeyFactory;

/**
 * Created by hackean on 4/30/17.
 */

public class KryptApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Realm realm = Realm.getDefaultInstance();

        PrimaryKeyFactory.getInstance().initialize(realm);
    }
}
