
package com.example.android.labmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Compound {

    @SerializedName("PropertyTable")
    @Expose
    private PropertyTable propertyTable;

    public PropertyTable getPropertyTable() {
        return propertyTable;
    }

    public void setPropertyTable(PropertyTable propertyTable) {
        this.propertyTable = propertyTable;
    }


    public String toString(String iupacName) {
        return iupacName;
    }
}
