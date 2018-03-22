package com.example.android.labmanager.ui.activity_list;

/**
 * Created by marcinek on 03.11.2017.
 */

public interface CompoundsListView {
    void displayCompoundsList();

    void showErrorMessage(int resId);

    void showNoCompound(int resId);

    void showWhereCompoundIs(int resId, String store);

    void displayQueriedCompound();

    void displayQueriedCompoundOptions();


}
