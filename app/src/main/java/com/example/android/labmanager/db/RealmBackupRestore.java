package com.example.android.labmanager.db;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.android.labmanager.App;
import com.example.android.labmanager.ui.activity_menu.MenuActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by marcin on 18.12.17.
 */

public class RealmBackupRestore extends MenuActivity {

    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_REALM_FILE_NAME = "labmanager.realm";
    private String IMPORT_REALM_FILE_NAME = "default.realm";

    private final static String TAG = RealmBackupRestore.class.getName();

    private Context context;
    private Realm realm;
    private DataBaseRealm dataBaseRealm;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Inject
    public RealmBackupRestore(DataBaseRealm dataBaseRealm, Context context) {
        this.dataBaseRealm = dataBaseRealm;
        this.context = context;


    }


    public void backup(Activity activity) {
        checkStoragePermissions(activity);

        realm = dataBaseRealm.getRealm();

        File exportRealmFile;

        Log.d(TAG, "Realm DB Path = " + realm.getPath());
        try {
            EXPORT_REALM_PATH.mkdirs();

            exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);

            exportRealmFile.delete();
            realm.writeCopyTo(exportRealmFile);

        } catch (io.realm.internal.IOException e) {
            e.printStackTrace();
        }
        String msg = "File exported to Path: " + EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, msg);

        realm.close();
    }


    public void restore(Activity activity) {
        checkStoragePermissions(activity);
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

        Log.d(TAG, "oldFilePath = " + restoreFilePath);

        copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME);
        Log.d(TAG, "Data restore is done");

        String msg = "File restored: " + restoreFilePath;
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {

        try {
            File file = new File(context.getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException | io.realm.internal.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


    }
}




