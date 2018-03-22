package com.example.android.labmanager.network.google;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;

import java.lang.ref.WeakReference;

/**
 * Created by marcinek on 28.12.2017.
 */

public class GoogleDriveBackup implements Backup, GoogleApiClient.OnConnectionFailedListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    @Nullable
    private GoogleApiClient googleApiClient;

    @Nullable
    GoogleApiAvailability googleAPI;


    @Nullable
    private WeakReference<Activity> activityRef;


    @Override
    public void init(@NonNull final Activity activity) {
        this.activityRef = new WeakReference<>(activity);


        googleApiClient = new GoogleApiClient.Builder(activity)//.setAccountName("marcintomala87@gmail.com")//.addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        // Do nothing
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public GoogleApiClient getClient() {
        return googleApiClient;
    }

    @Override
    public boolean checkGoogleAvalibility(Activity activity) {

         googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    @Override
    public void start() {

        if (!googleApiClient.equals(null)) {
            googleApiClient.connect();


        } else {
            throw new IllegalStateException("You should call init before start");
        }
    }

    @Override
    public void stop() {
        if (googleApiClient != null) {
            signOut();

        } else {
            throw new IllegalStateException("You should call init before start");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult result) {
        Log.i("Connection Failed", "GoogleApiClient connection failed: " + result.toString());

        if (result.hasResolution() && activityRef != null && activityRef.get() != null) {
            Activity a = activityRef.get();
            // shows the localized error dialog.
            try {
                result.startResolutionForResult(a, 1);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                GoogleApiAvailability.getInstance().getErrorDialog(a, result.getErrorCode(), 0).show();
            }
        } else {
            Log.d("error", "cannot resolve connection issue");
        }
    }

    void signOut() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {

                @Override
                public void onResult(Status status) {

                    googleApiClient.disconnect();
                }
            });

        }
    }
}