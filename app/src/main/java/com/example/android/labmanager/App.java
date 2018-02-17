package com.example.android.labmanager;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.android.labmanager.db.Backup;
import com.example.android.labmanager.db.DataBaseRealm;
import com.example.android.labmanager.db.GoogleDriveBackup;
import com.example.android.labmanager.di.AppComponent;
import com.example.android.labmanager.di.AppModule;
import com.example.android.labmanager.di.DaggerAppComponent;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.regex.Pattern;

import dagger.Module;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sda on 05.07.17.
 */
@Module
public class App extends Application {


    private AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
      initDagger();
        Stetho.initializeWithDefaults(this);
        buildStethoRealm();
        //   buildRealmInspector();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

  private void initDagger() {
   appComponent = DaggerAppComponent.builder()
           .appModule(new AppModule(this))
          .build();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    private void buildStethoRealm() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }

    private void buildRealmInspector() {
        RealmInspectorModulesProvider.builder(this)
                .withFolder(getCacheDir())
                // .withEncryptionKey("encrypted.realm", )
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();
    }

    @NonNull
    public Backup getBackup() {
        return new GoogleDriveBackup();
    }


    @NonNull
    public DataBaseRealm getDBHandler() {
        return new DataBaseRealm(getApplicationContext());
    }



}
