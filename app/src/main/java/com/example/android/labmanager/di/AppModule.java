package com.example.android.labmanager.di;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.labmanager.db.Backup;
import com.example.android.labmanager.db.DataBaseRealm;
import com.example.android.labmanager.db.GoogleDriveBackup;
import com.example.android.labmanager.db.RealmBackupRestore;
import com.example.android.labmanager.network.PubChemClient;
import com.example.android.labmanager.ui.activity_backup.BackupPresenter;
import com.example.android.labmanager.utilis.SharedPrefStorage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * Created by Admin on 2017-07-19.
 */

@dagger.Module
public class AppModule {

    private Context context;


    public AppModule(Context context) {
        this.context = context;
    }


    @Provides
    @Singleton
    Context providesContext() {
        return context;
    }

    @Provides
    @Singleton
    PubChemClient providesApiClient() {
        return new PubChemClient();
    }


    @Provides
    @Singleton
    SharedPrefStorage providesSharedPrefStorage(Context context) {
        return new SharedPrefStorage(context);
    }

    @Provides
    @Singleton
    DataBaseRealm providesDataBaseRealm(Context context) {
        return new DataBaseRealm(context);
    }

    @Provides
    @Singleton
    RealmBackupRestore providesRealmBackupRestore(DataBaseRealm dataBaseRealm, Context context) {
        return new RealmBackupRestore(dataBaseRealm, context);
    }



}






