package com.example.android.labmanager.ui.activity_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.labmanager.App;
import com.example.android.labmanager.R;
import com.example.android.labmanager.adapters.RecyclerItemClickListener;
import com.example.android.labmanager.ui.activity_menu.MenuActivity;
import com.example.android.labmanager.ui.activity_menu.MenuDrawer;
import com.example.android.labmanager.ui.activity_property_card.PropertyCardActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Admin on 2017-07-27.
 */

public class CompoundsListActivity extends MenuActivity implements CompoundsListView, MenuDrawer {

    boolean touchOn;
    RecyclerItemClickListener recyclerItemClickListener;

    @Nullable
    @BindView(R.id.compounds_recycler)
    RecyclerView recycler;

    @Nullable
    @BindView(R.id.lookForCid)
    EditText cidQuery;

    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;

    @Optional
    @OnClick(R.id.checkCompoundAvailable)

    public void onClick2() {
        compoundsListPresenter.checkCompoundIsStored(String.valueOf(cidQuery.getText()));
        compoundsListPresenter.displayQueriedCompound();
        setRecyclerViewTouchable();
    }

    @Optional
    @OnClick(R.id.reloadList)
    public void onClick3() {
        compoundsListPresenter.displayCompoundsList();
        touchOn = false;
    }

    @Inject
    CompoundsListPresenter compoundsListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        invokeMenuDrawer();
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        setTitle();
        compoundsListPresenter.attachListView(this);
        compoundsListPresenter.displayCompoundsList();
    }

    @Override
    public void invokeMenuDrawer() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list, null, false);
        drawer.addView(contentView, 0);
    }

    @Override
    public void setTitle() {
        textViewToolbar.setText("Compounds List");
    }


    @Override
    protected void onStart() {

        super.onStart();
        Log.e("STATE", "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("STATE", "OnResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("STATE", "OnStop");
    }

    @Override
    protected void onDestroy() {
        Log.e("STATE", "OnDestroy");
        compoundsListPresenter.closeRealm();
        compoundsListPresenter.detachListView();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PropertyCardActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void displayCompoundsList() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(compoundsListPresenter.showCompoundsList());
    }

    @Override
    public void displayQueriedCompound() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(compoundsListPresenter.showSearchedCompound());
    }

    @Override
    public void displayQueriedCompoundOptions() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(compoundsListPresenter.showSearchedCompound2());
    }


    @Override
    public void showErrorMessage(int resId) {
        Toast.makeText(getApplicationContext(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoCompound(int resId) {
        Toast.makeText(getApplicationContext(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWhereCompoundIs(int resId, String store) {
        Toast.makeText(getApplicationContext(), getString(resId) + " " + store, Toast.LENGTH_SHORT).show();
    }


    void setRecyclerViewTouchable() {
        touchOn = true;
        recyclerItemClickListener = new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                while (touchOn) {
                    compoundsListPresenter.displayQueriedCompoundOptions();
                    break;
                }
            }
        });
        recycler.addOnItemTouchListener(recyclerItemClickListener);

    }


}




