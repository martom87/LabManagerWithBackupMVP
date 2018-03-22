package com.example.android.labmanager.utilis;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.labmanager.model.Property;
import com.google.gson.Gson;

/**
 * Created by marcinek on 01.11.2017.
 */

public class SharedPrefStorage {


    SharedPreferences sharedPref;


    private static final String PREFS_KEY = "com.example.android.labmanager.sharedPref";
    private static final String PROPERTY_KEY = "property_key";


    public SharedPrefStorage(Context context) {
        sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
    }

    public void writeProperty(Property property) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PROPERTY_KEY, wrapProperty(property));
        editor.commit();

    }

    public Property readProperty() {
        String wrappedProperty = sharedPref.getString(PROPERTY_KEY, null);
        return unwrapProperty(wrappedProperty);
    }


    public String wrapProperty(Property property) {
        Gson gson = new Gson();
        String wrappedProperty = gson.toJson(property);
        return wrappedProperty;
    }


    public Property unwrapProperty(String wrappedProperty) {
        Gson gson = new Gson();
        Property property = gson.fromJson(wrappedProperty, Property.class);
        return property;
    }


}
