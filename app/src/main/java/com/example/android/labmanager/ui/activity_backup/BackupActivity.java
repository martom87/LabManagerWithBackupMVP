package com.example.android.labmanager.ui.activity_backup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.labmanager.App;
import com.example.android.labmanager.R;
import com.example.android.labmanager.adapters.BackupAdapter;
import com.example.android.labmanager.db.LabManagerBackup;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.drive.DriveId;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BackupActivity extends AppCompatActivity implements BackupView {


    @OnClick(R.id.activity_backup_drive_button_backup)
    public void onClick() {

        backupPresenter.openFolderPicker(true, this);
    }

    @OnClick(R.id.activity_backup_drive_button_manage_drive)
    public void onClick2() {

        backupPresenter.openOnDrive(DriveId.decodeFromString(backupPresenter.getBackupFolder()), this);
    }

    @BindView(R.id.activity_backup_drive_button_manage_drive)
    TextView manageButton;

    @BindView(R.id.activity_backup_drive_textview_folder)
    TextView folderTextView;

    @OnClick(R.id.activity_backup_drive_button_folder)

    public void onClick3() {
        if (!"".equals(backupPresenter.getBackupFolder())) {


            backupPresenter.openFolderPicker(false, this);
        }
    }

    @BindView(R.id.activity_backup_drive_listview_restore)
    ExpandableHeightListView backupListView;

    @Inject
    BackupPresenter backupPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_drive_activity);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);

        backupPresenter.attachBackupView(this);
        backupPresenter.initialize(this);
     //   backupPresenter.showBackupFolder();
     //   backupPresenter.populateBackupList();

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
    public void showSuccessDialog() {
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorDialog() {
        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
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
        backupPresenter.performOnActivityResult(this);

    }


}

