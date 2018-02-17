package com.example.android.labmanager.ui.activity_backup;

import com.example.android.labmanager.db.LabManagerBackup;

import java.util.ArrayList;

/**
 * Created by marcinek on 10.02.2018.
 */

public interface BackupView {

    void showSuccessDialog();

    void showErrorDialog();

    void setManageButtonVisible();

    void showTitle(String title);

    void setBackupAdapter();

    void makeBackUpListViewExpandalbe();



}
