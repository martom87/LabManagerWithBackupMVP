package com.example.android.labmanager.adapters;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.labmanager.R;
import com.example.android.labmanager.utilis.FormatDateTime;
import com.example.android.labmanager.model.LabManagerBackup;
import com.example.android.labmanager.ui.activity_backup.BackupPresenter;
import com.google.android.gms.drive.DriveId;


import java.util.List;

import javax.inject.Inject;

public class BackupAdapter extends ArrayAdapter<LabManagerBackup> {


    private Context context;
    private FormatDateTime formatDateTime;

    BackupPresenter backupPresenter;

    @Inject
    public BackupAdapter(Context context, int resource, List<LabManagerBackup> items, BackupPresenter backupPresenter) {
        super(context, resource, items);
        this.context = context;
        this.backupPresenter = backupPresenter;
        formatDateTime = new FormatDateTime(context);
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.activity_backup_drive_restore_item, parent, false);
        }

        LabManagerBackup labManagerBackupItem = getItem(position);
        if (labManagerBackupItem != null) {
            final DriveId driveId = labManagerBackupItem.getDriveId();
            final String backupDate = formatDateTime.formatDate(labManagerBackupItem.getModifiedDate());
            final String backupSize = Formatter.formatFileSize(getContext(), labManagerBackupItem.getBackupSize());

            TextView modifiedTextView = (TextView) view.findViewById(R.id.item_history_time);
            TextView typeTextView = (TextView) view.findViewById(R.id.item_history_type);
            modifiedTextView.setText(backupDate);
            typeTextView.setText(backupSize);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Shows custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_backup_restore);
                    TextView createdTextView = (TextView) dialog.findViewById(R.id.dialog_backup_restore_created);
                    TextView sizeTextView = (TextView) dialog.findViewById(R.id.dialog_backup_restore_size);
                    Button restoreButton = (Button) dialog.findViewById(R.id.dialog_backup_restore_button_restore);
                    Button cancelButton = (Button) dialog.findViewById(R.id.dialog_backup_restore_button_cancel);

                    createdTextView.setText(backupDate);
                    sizeTextView.setText(backupSize);

                    restoreButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            backupPresenter.downloadFromDrive(driveId.asDriveFile());
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }

        return view;
    }

}
