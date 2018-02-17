package com.example.android.labmanager.ui.activity_backup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.labmanager.App;
import com.example.android.labmanager.R;
import com.example.android.labmanager.adapters.BackupAdapter;
import com.example.android.labmanager.db.Backup;
import com.example.android.labmanager.db.LabManagerBackup;
import com.example.android.labmanager.di.AppComponent;
import com.example.android.labmanager.ui.activity_menu.MenuActivity;
import com.example.android.labmanager.ui.activity_query.QueryView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import io.realm.Realm;

import static android.app.Activity.RESULT_OK;

/**
 * Created by marcinek on 10.02.2018.
 */

public class BackupPresenter  {

    private static final int REQUEST_CODE_PICKER = 2;
    private static final int REQUEST_CODE_PICKER_FOLDER = 4;

    private static final String TAG = "labmanager_drive_backup";
    private static final String BACKUP_FOLDER_KEY = "backup_folder";

    private IntentSender intentPicker;
    private String backupFolder;

    private GoogleApiClient mGoogleApiClient;
    private Backup backup;
    private Realm realm;
    private SharedPreferences sharedPref;

    private App labManagerApp;
    private BackupView backupView;
    String title;
    private ArrayList<LabManagerBackup> backupsArray = new ArrayList<>();

 //   @Nullable
 //   private WeakReference<Activity> activityRef;

    @Inject
    public BackupPresenter() {
    }

    public void attachBackupView(BackupView backupView) {
        this.backupView = backupView;
    }

    public void detachBackupView() {
        this.backupView = null;
    }


    void initialize(@NonNull final Activity activity) {
    //    this.activityRef = new WeakReference<Activity>(activity);

        labManagerApp = (App) activity.getApplicationContext();
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        realm = labManagerApp.getDBHandler().getRealm();
        backup = labManagerApp.getBackup();
        backup.init(activity);
        connectClient();
        mGoogleApiClient = backup.getClient();

        backupFolder = sharedPref.getString(BACKUP_FOLDER_KEY, "");
        showBackupFolder();
        populateBackupList();

    }


    void showBackupFolder() {

        if (!("").equals(backupFolder)) {
            setBackupFolderTitle(DriveId.decodeFromString(backupFolder));
            backupView.setManageButtonVisible();
        }
    }


    void populateBackupList() {

        if (!("").equals(backupFolder)) {
            getBackupsFromDrive(DriveId.decodeFromString(backupFolder).asDriveFolder());
        }
    }


    private void setBackupFolderTitle(DriveId id) {
        id.asDriveFolder().getMetadata((mGoogleApiClient)).setResultCallback(
                new ResultCallback<DriveResource.MetadataResult>() {
                    @Override
                    public void onResult(@NonNull DriveResource.MetadataResult result) {
                        if (!result.getStatus().isSuccess()) {
                            backupView.showErrorDialog();
                            return;
                        }
                        Metadata metadata = result.getMetadata();


                        title = metadata.getTitle();
                        backupView.showTitle(title);
                    }
                }
        );
    }

    String getTitle() {
        return title;
    }


    String getBackupFolder(){
        return backupFolder;
    }

    void openFolderPicker(boolean uploadToDrive, @NonNull final Activity activity) {
      //  this.activityRef = new WeakReference<>(activity);
        if (uploadToDrive) {
            // First we check if a backup folder is set
            if (TextUtils.isEmpty(backupFolder)) {
                try {
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        if (intentPicker == null)
                            intentPicker = buildIntent();
                        //Start the picker to choose a folder
                        activity.startIntentSenderForResult(
                                intentPicker, REQUEST_CODE_PICKER, null, 0, 0, 0);

                    }
                    //check client is connected
                    String a = String.valueOf(mGoogleApiClient != null);
                    String b = String.valueOf(mGoogleApiClient.isConnected());
                    Toast.makeText(labManagerApp, a + "/" + b, Toast.LENGTH_SHORT).show();
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Unable to send intent", e);
                    backupView.showErrorDialog();
                }
            } else {
                uploadToDrive(DriveId.decodeFromString(backupFolder), activity);
            }
        } else {
            try {
                intentPicker = null;
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    if (intentPicker == null)
                        intentPicker = buildIntent();
                    //Start the picker to choose a folder
                    activity.startIntentSenderForResult(
                            intentPicker, REQUEST_CODE_PICKER_FOLDER, null, 0, 0, 0);
                }
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Unable to send intent", e);
                backupView.showErrorDialog();
            }
        }
    }


    private IntentSender buildIntent() {
        return Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{DriveFolder.MIME_TYPE})
                .build(mGoogleApiClient);
    }

    private void getBackupsFromDrive(DriveFolder folder) {

        SortOrder sortOrder = new SortOrder.Builder()
                .addSortDescending(SortableField.MODIFIED_DATE).build();
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "labmanager.realm"))
                .addFilter(Filters.eq(SearchableField.TRASHED, false))
                .setSortOrder(sortOrder)
                .build();
        folder.queryChildren(mGoogleApiClient, query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {


                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult result) {
                        MetadataBuffer buffer = result.getMetadataBuffer();
                        int size = buffer.getCount();
                        for (int i = 0; i < size; i++) {
                            Metadata metadata = buffer.get(i);
                            DriveId driveId = metadata.getDriveId();
                            Date modifiedDate = metadata.getModifiedDate();
                            long backupSize = metadata.getFileSize();
                            backupsArray.add(new LabManagerBackup(driveId, modifiedDate, backupSize));
                        }

                        backupView.setBackupAdapter();
                    }
                });
    }


    public ArrayList<LabManagerBackup> getBackupsArray() {
        return backupsArray;
    }

    public void downloadFromDrive(DriveFile file) {
        file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            backupView.showErrorDialog();
                            return;
                        }

                        // DriveContents object contains pointers
                        // to the actual byte stream
                        DriveContents contents = result.getDriveContents();
                        InputStream input = contents.getInputStream();

                        try {
                            File file = new File(realm.getPath());
                            OutputStream output = new FileOutputStream(file);
                            try {
                                try {
                                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                                    int read;

                                    while ((read = input.read(buffer)) != -1) {
                                        output.write(buffer, 0, read);
                                    }
                                    output.flush();
                                } finally {
                                    safeCloseClosable(input);
                                }
                            } catch (Exception e) {
                                reportToFirebase(e, "Error downloading backup from drive");
                                e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            reportToFirebase(e, "Error downloading backup from drive, file not found");
                            e.printStackTrace();
                        } finally {
                            safeCloseClosable(input);
                        }

                        Toast.makeText(labManagerApp, "restart", Toast.LENGTH_LONG).show();

                        // Reboot app
                        Intent mStartActivity = new Intent(labManagerApp, MenuActivity.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(labManagerApp, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) labManagerApp.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    }
                });
    }

    private void safeCloseClosable(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            reportToFirebase(e, "Error downloading backup from drive, IO Exception");
            e.printStackTrace();
        }
    }

     void uploadToDrive(DriveId mFolderDriveId, @NonNull final Activity activity) {
      //  this.activityRef = new WeakReference<Activity>(activity);
        if (mFolderDriveId != null) {
            //Create the file on GDrive
            final DriveFolder folder = mFolderDriveId.asDriveFolder();
            Drive.DriveApi.newDriveContents(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                            if (!result.getStatus().isSuccess()) {
                                Log.e(TAG, "Error while trying to create new file contents");
                                backupView.showErrorDialog();
                                return;
                            }
                            final DriveContents driveContents = result.getDriveContents();

                            // Perform I/O off the UI thread.
                            new Thread() {
                                @Override
                                public void run() {
                                    // write content to DriveContents
                                    OutputStream outputStream = driveContents.getOutputStream();

                                    FileInputStream inputStream = null;
                                    try {
                                        inputStream = new FileInputStream(new File(realm.getPath()));
                                    } catch (FileNotFoundException e) {
                                        reportToFirebase(e, "Error uploading backup from drive, file not found");
                                        backupView.showErrorDialog();
                                        e.printStackTrace();
                                    }

                                    byte[] buf = new byte[1024];
                                    int bytesRead;
                                    try {
                                        if (inputStream != null) {
                                            while ((bytesRead = inputStream.read(buf)) > 0) {
                                                outputStream.write(buf, 0, bytesRead);
                                            }
                                        }
                                    } catch (IOException e) {

                                        backupView.showErrorDialog();
                                        e.printStackTrace();
                                    }


                                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                            .setTitle("labmanager.realm")
                                            .setMimeType("text/plain")
                                            .build();

                                    // create a file in selected folder
                                    folder.createFile(mGoogleApiClient, changeSet, driveContents)
                                            .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                                @Override
                                                public void onResult(@NonNull DriveFolder.DriveFileResult result) {
                                                    if (!result.getStatus().isSuccess()) {
                                                        Log.d(TAG, "Error while trying to create the file");
                                                        backupView.showErrorDialog();
                                                        activity.finish();
                                                        return;
                                                    }
                                                    backupView.showSuccessDialog();
                                                    activity.finish();
                                                }
                                            });
                                }
                            }.start();
                        }
                    });
        }
    }



     void openOnDrive(DriveId driveId, @NonNull final Activity activity) {

    //    this.activityRef = new WeakReference<Activity>(activity);
        driveId.asDriveFolder().getMetadata((mGoogleApiClient)).setResultCallback(
                new ResultCallback<DriveResource.MetadataResult>() {
                    @Override
                    public void onResult(@NonNull DriveResource.MetadataResult result) {
                        if (!result.getStatus().isSuccess()) {
                            backupView.showErrorDialog();
                            return;
                        }
                        Metadata metadata = result.getMetadata();
                        String url = metadata.getAlternateLink();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        activity.startActivity(i);
                    }
                }
        );
    }


    protected void performOnActivityResult( @Nullable final Activity activity) {
        final int requestCode = 0;
        final int resultCode = 0;
        final Intent data = null;
       // this.activityRef = new WeakReference<Activity>(activity);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    backup.start();
                }
                break;
            // REQUEST_CODE_PICKER
            case 2:
                intentPicker = null;

                if (resultCode == RESULT_OK) {
                    //Get the folder drive id
                    DriveId mFolderDriveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    saveBackupFolder(mFolderDriveId.encodeToString());

                    uploadToDrive(mFolderDriveId, activity);
                }
                break;

            // REQUEST_CODE_SELECT
            case 3:
                if (resultCode == RESULT_OK) {
                    // get the selected item's ID
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    DriveFile file = driveId.asDriveFile();
                    downloadFromDrive(file);

                } else {
                    backupView.showErrorDialog();
                }
                activity.finish();
                break;
            // REQUEST_CODE_PICKER_FOLDER
            case 4:
                if (resultCode == RESULT_OK) {
                    //Get the folder drive id
                    DriveId mFolderDriveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    saveBackupFolder(mFolderDriveId.encodeToString());
                    // Restart activity to apply changes
                    Intent intent = activity.getIntent();
                    activity.finish();
                    activity.startActivity(intent);
                }
                break;
        }
    }

    IntentSender getIntendPicker(){
        return intentPicker;
    }

    Backup getBackup(){
        return  backup;
    }


    void saveBackupFolder(String folderPath) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(BACKUP_FOLDER_KEY, folderPath);
        editor.apply();
    }


    private void reportToFirebase(Exception e, String message) {
        //  FirebaseCrash.log(message);
        //  FirebaseCrash.report(e);
    }

    public void connectClient() {
        backup.start();
    }

    public void disconnectClient() {
        backup.stop();
    }

    public boolean onOptionsItemSelected(MenuItem item, @Nullable final Activity activity) {
      //  this.activityRef = new WeakReference<Activity>(activity);
        activity.finish();
        return true;
    }


}
