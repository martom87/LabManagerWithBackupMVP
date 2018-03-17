package com.example.android.labmanager.ui.activity_menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.labmanager.R;
import com.example.android.labmanager.ui.activity_backup.BackupActivity;
import com.example.android.labmanager.ui.activity_list.CompoundsListActivity;
import com.example.android.labmanager.ui.activity_property_card.PropertyCardActivity;
import com.example.android.labmanager.ui.activity_query.QueryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ActionBarDrawerToggle toggle;


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        onCreateDrawer();


    }

    protected void onCreateDrawer() {

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_compoundsList) {
            Intent intent = new Intent(this, CompoundsListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_query) {
            Intent intent = new Intent(this, QueryActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_queriedCompoundCard) {
            Intent intent = new Intent(this, PropertyCardActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_makeGoogleBackup) {
            Intent intent = new Intent(this, BackupActivity.class);
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}


