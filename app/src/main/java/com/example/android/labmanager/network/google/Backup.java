package com.example.android.labmanager.network.google;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by marcinek on 09.01.2018.
 */

public interface Backup {

    void init(@NonNull final Activity activity);

    void start();

    void stop();

    GoogleApiClient getClient();

    boolean checkGoogleAvalibility(Activity activity);

}
