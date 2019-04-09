package com.nsa.adminapp;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private boolean isDrawerVisible = false;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        if (!isDrawerVisible) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.start_fragment, new LoginScreen()).commit();
            }
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggleDrawer(toolbar);


    }

    public void toggleDrawer(Toolbar toolbar) {
        if (isDrawerVisible) {
            drawerLayout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (id == R.id.log_out) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new LoginScreen()).commit();
            navigationView.setCheckedItem(R.id.login_fragment);
            isDrawerVisible = true;
            finish();
            startActivity(getIntent());
        }
        if (id == R.id.heat_map) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new Maps(), "heatMap").commit();
            navigationView.setCheckedItem(R.id.heat_map);
        }if (id == R.id.section_1) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new HomePage()).commit();
            navigationView.setCheckedItem(R.id.section_1);

        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setDrawerVisible(boolean drawerVisible) {
        isDrawerVisible = drawerVisible;
    }


}
