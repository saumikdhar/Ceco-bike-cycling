package com.nsa.cecobike;

import android.arch.persistence.room.Room;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new Map()).commit();
            navigationView.setCheckedItem(R.id.nav_track_my_journey);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        NavigationView navigationView = findViewById(R.id.nav_view);

        JourneyDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                JourneyDatabase.class,
                "JourneyDatabase"
        ).build();

        if (id == R.id.nav_track_my_journey) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new Map()).commit();
            navigationView.setCheckedItem(R.id.nav_track_my_journey);
        } else if (id == R.id.nav_view_my_journeys) {
//            Toast.makeText(this, "View_my_journey fragment has not been created yet", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.start_fragment, new VIewMyJourney()).commit();
                navigationView.setCheckedItem(R.id.nav_view_my_journeys);
        } else if (id == R.id.nav_set_goal) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new SetGoal()).commit();
            navigationView.setCheckedItem(R.id.nav_set_goal);
        } else if (id == R.id.nav_about_us)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.start_fragment, new AboutUs()).commit();
            navigationView.setCheckedItem(R.id.nav_about_us);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
