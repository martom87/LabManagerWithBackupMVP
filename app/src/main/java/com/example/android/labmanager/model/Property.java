
package com.example.android.labmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@SuppressWarnings("serial")


public class Property extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("CID")
    @Expose
    private Integer cID;

    @SerializedName("MolecularFormula")
    @Expose
    private String molecularFormula;

    @SerializedName("MolecularWeight")
    @Expose
    private Double molecularWeight;


    @SerializedName("IUPACName")
    @Expose
    private String iUPACName;

    private String store;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Integer getCID() {
        return cID;
    }

    public void setCID(Integer cID) {
        this.cID = cID;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public Double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(Double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getIUPACName() {
        return iUPACName;
    }

    public void setIUPACName(String iUPACName) {
        this.iUPACName = iUPACName;
    }


}
