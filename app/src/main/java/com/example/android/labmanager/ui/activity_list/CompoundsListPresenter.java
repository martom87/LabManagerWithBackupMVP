package com.example.android.labmanager.ui.activity_list;

import com.example.android.labmanager.R;
import com.example.android.labmanager.adapters.RealmCompoundAdapter;
import com.example.android.labmanager.adapters.RealmCompoundAdapter2;
import com.example.android.labmanager.db.DataBaseRealm;
import com.example.android.labmanager.model.Property;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by marcinek on 03.11.2017.
 */

public class CompoundsListPresenter {
    private CompoundsListView compoundsListView;
    private DataBaseRealm dataBaseRealm;
    private Realm realm;
    private Property query;
    private Integer cid2;

    @Inject
    public CompoundsListPresenter(DataBaseRealm dataBaseRealm) {
        this.dataBaseRealm = dataBaseRealm;
    }

    public void attachListView(CompoundsListView compoundsListView) {
        this.compoundsListView = compoundsListView;
    }

    public void detachListView() {
        this.compoundsListView = null;
    }


    RealmCompoundAdapter showCompoundsList() {

        RealmCompoundAdapter adapter = dataBaseRealm.displayList();
        return adapter;
    }

    RealmCompoundAdapter showSearchedCompound() {
        RealmCompoundAdapter adapter = dataBaseRealm.displayQueriedItem(cid2);
        return adapter;
    }

    RealmCompoundAdapter2 showSearchedCompound2() {
        RealmCompoundAdapter2 adapter2 = dataBaseRealm.displayQueriedItem2(cid2);
        return adapter2;
    }

    void displayCompoundsList() {

        compoundsListView.displayCompoundsList();
    }

    void displayQueriedCompound() {
        compoundsListView.displayQueriedCompound();
    }

    void displayQueriedCompoundOptions() {
        compoundsListView.displayQueriedCompoundOptions();
    }


    Realm getRealmInstance() {
        Realm realm = dataBaseRealm.getRealm();
        return realm;
    }

    public void closeRealm() {
        Realm realm = dataBaseRealm.getRealm();
        realm.close();
    }


    public void checkPropertyExsists(String queriedCid) {
        realm = getRealmInstance();
        cid2 = Integer.valueOf(queriedCid);
        if (realm.where(Property.class).equalTo("cID", cid2).count() == 0) {

            compoundsListView.showNoCompound(R.string.infoNoCompound);
        } else {

            findProperty();
            compoundsListView.showWhereCompoundIs(R.string.infoCompoundIsIn, findProperty().getStore());

        }
    }

    public void checkCompoundIsStored(String queriedCid) {

        if (queriedCid.isEmpty() || (!queriedCid.matches("-?\\d+(\\.\\d+)?")) || queriedCid.matches("[^123456789]+")) {
            compoundsListView.showErrorMessage(R.string.errorMessage);
        } else {

            checkPropertyExsists(queriedCid);

        }
    }

    public Property findProperty() {
        realm = getRealmInstance();
        query = realm.where(Property.class).equalTo("cID", cid2).findFirst();
        return query;

    }


}
