package com.example.android.labmanager.ui.activity_property_card;

/**
 * Created by marcinek on 30.10.2017.
 */

public interface PropertyCardView {

    void displayPropertyData();

    void displayPropertyImage(String url);

    void askIfOverrideCompound();

    void showCompoundWasSaved(int resId);

    void showErrorMessage(int resId);

}
