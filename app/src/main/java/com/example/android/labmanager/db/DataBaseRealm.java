package com.example.android.labmanager.db;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.example.android.labmanager.App;
import com.example.android.labmanager.adapters.RealmCompoundAdapter;
import com.example.android.labmanager.adapters.RealmCompoundAdapter2;
import com.example.android.labmanager.model.Property;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by marcinek on 03.11.2017.
 */

public class DataBaseRealm {

    private RealmCompoundAdapter compoundAdapter;
    private RealmCompoundAdapter2 compoundAdapter2;
    private Realm realm;
    private RealmResults<Property> propertyRealmResults;
    private Context context;


    public DataBaseRealm(Context context) {
    }

    public void saveCompoundsDataToDb(final Property property) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(property);
            }
        });

    }

    public void deleteCompoundsFromDb(final Integer deleteCid) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                propertyRealmResults = realm.where(Property.class).equalTo("cID", deleteCid).findAll();
                propertyRealmResults.deleteAllFromRealm();
            }
        });
    }

    public RealmCompoundAdapter displayList() {
        realm = Realm.getDefaultInstance();
        propertyRealmResults = realm.where(Property.class).findAll();
        compoundAdapter = new RealmCompoundAdapter(propertyRealmResults, true);
        return compoundAdapter;
    }


    public RealmCompoundAdapter displayQueriedItem(Integer cid2) {
        realm = Realm.getDefaultInstance();
        propertyRealmResults = realm.where(Property.class).equalTo("cID", cid2).findAll();
        compoundAdapter = new RealmCompoundAdapter(propertyRealmResults, true);
        return compoundAdapter;
    }

    public RealmCompoundAdapter2 displayQueriedItem2(Integer cid2) {
        realm = Realm.getDefaultInstance();
        propertyRealmResults = realm.where(Property.class).equalTo("cID", cid2).findAll();

        compoundAdapter2 = new RealmCompoundAdapter2(propertyRealmResults, true, this);
        return compoundAdapter2;
    }

    public Realm getRealm() {
        realm = Realm.getDefaultInstance();
        return realm;
    }


}

