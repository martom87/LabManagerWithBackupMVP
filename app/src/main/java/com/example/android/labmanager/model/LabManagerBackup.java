

package com.example.android.labmanager.model;

import com.google.android.gms.drive.DriveId;

import java.util.Date;


public class LabManagerBackup {

    private DriveId driveId;
    private Date modifiedDate;
    private long backupSize;

    public LabManagerBackup(DriveId driveId, Date modifiedDate, long backupSize) {
        this.driveId = driveId;
        this.modifiedDate = modifiedDate;
        this.backupSize = backupSize;
    }

    public DriveId getDriveId() {
        return driveId;
    }

    public void setDriveId(DriveId driveId) {
        this.driveId = driveId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public long getBackupSize() {
        return backupSize;
    }

    public void setBackupSize(long backupSize) {
        this.backupSize = backupSize;
    }
}
