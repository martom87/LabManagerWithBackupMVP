package com.example.android.labmanager.ui.activity_backup;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.labmanager.App;
import com.example.android.labmanager.R;
import com.example.android.labmanager.adapters.BackupAdapter;
import com.example.android.labmanager.model.LabManagerBackup;
import com.example.android.labmanager.ui.activity_menu.MenuActivity;
import com.example.android.labmanager.ui.activity_menu.MenuDrawer;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class BackupActivity extends MenuActivity implements BackupView, MenuDrawer {


    @Optional
    @OnClick(R.id.activity_backup_drive_button_backup)
    public void onClick() {

        backupPresenter.openFolderPicker(true, this);
    }

    @Optional
    @OnClick(R.id.activity_backup_drive_button_manage_drive)
    public void onClick2() {

        backupPresenter.openOnDrive(DriveId.decodeFromString(backupPresenter.getBackupFolder()), this);
    }

    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;

    @Nullable
    @BindView(R.id.activity_backup_drive_button_manage_drive)
    TextView manageButton;

    @Nullable
    @BindView(R.id.activity_backup_drive_textview_folder)
    TextView folderTextView;

    @Optional
    @OnClick(R.id.activity_backup_drive_button_folder)

    public void onClick3() {
        if (!"".equals(backupPresenter.getBackupFolder())) {


            backupPresenter.openFolderPicker(false, this);
        }
    }

    @Nullable
    @BindView(R.id.activity_backup_drive_listview_restore)
    ExpandableHeightListView backupListView;

    @Inject
    BackupPresenter backupPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeMenuDrawer();
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        setTitle();

        backupPresenter.attachBackupView(this);
        backupPresenter.initialize(this);


        makeBackUpListViewExpandalbe();


    }

    @Override
    protected void onDestroy() {
        Log.e("STATE", "OnDestroy");
        backupPresenter.detachBackupView();
        backupPresenter.disconnectClient();
        super.onDestroy();
    }

    @Override
    public void invokeMenuDrawer() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.backup_drive_activity, null, false);
        drawer.addView(contentView, 0);
    }

    @Override
    public void setTitle() {
        textViewToolbar.setText("Backup");
    }


    @Override
    public void showSuccessDialog() {
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorDialog() {
        Toast.makeText(getApplicationContext(), "Failure, Please select BackupFolder", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setManageButtonVisible() {

        manageButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void showTitle(String title) {
        title = backupPresenter.getTitle();
        folderTextView.setText(title);
    }

    @Override
    public void setBackupAdapter() {
        ArrayList<LabManagerBackup> backupsArray = backupPresenter.getBackupsArray();
        backupListView.setAdapter(new BackupAdapter(this, R.layout.activity_backup_drive_restore_item, backupsArray, backupPresenter));
    }

    @Override
    public void makeBackUpListViewExpandalbe() {
        backupListView.setExpanded(true);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    backupPresenter.getBackup().start();

                }
                break;

            case 2:
                IntentSender intentPicker = backupPresenter.getIntendPicker();
                intentPicker = null;


                if (resultCode == RESULT_OK) {
                    //Get the folder drive id
                    DriveId mFolderDriveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    backupPresenter.saveBackupFolder(mFolderDriveId.encodeToString());

                    backupPresenter.uploadToDrive(mFolderDriveId, this);
                }
                break;

            // REQUEST_CODE_SELECT
            case 3:
                if (resultCode == RESULT_OK) {
                    // get the selected item's ID
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    DriveFile file = driveId.asDriveFile();
                    backupPresenter.downloadFromDrive(file);

                } else {
                    showErrorDialog();
                }
                finish();
                break;
            // REQUEST_CODE_PICKER_FOLDER
            case 4:
                if (resultCode == RESULT_OK) {
                    //Get the folder drive id
                    DriveId mFolderDriveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    backupPresenter.saveBackupFolder(mFolderDriveId.encodeToString());
                    // Restart activity to apply changes
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                break;
        }
    }


}

