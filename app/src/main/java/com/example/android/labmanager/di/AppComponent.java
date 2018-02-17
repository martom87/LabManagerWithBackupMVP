package com.example.android.labmanager.di;

import com.example.android.labmanager.ui.activity_backup.BackupActivity;
import com.example.android.labmanager.ui.activity_list.CompoundsListActivity;
import com.example.android.labmanager.ui.activity_property_card.PropertyCardActivity;
import com.example.android.labmanager.ui.activity_query.QueryActivity;

import javax.inject.Singleton;

/**
 * Created by Admin on 2017-07-19.
 */
@Singleton
@dagger.Component(modules = {AppModule.class})

public interface AppComponent {

    void inject(QueryActivity queryActivity);

    void inject(PropertyCardActivity propertyCardActivity);

    void inject(CompoundsListActivity compoundsListActivity);

    void inject(BackupActivity backupActivity2);




}
