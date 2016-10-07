package com.example.vinit.sicsreventdesertation;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import java.util.HashMap;

public class HomeNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CharSequence mTitle;
    FragmentManager fm=getSupportFragmentManager();
    AlertDialog.Builder alertDialogBuilder;
    TextView txtHeaderName,txtHeaderEmail;
    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);

        alertDialogBuilder = new AlertDialog.Builder(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new UserSessionManager(getApplicationContext());
//        if(session.checkLogin())
//            finish();
        Home home=new Home();
        fm.beginTransaction()
                .replace(R.id.fragmentcontainer,home)
                .commit();

        HashMap<String, String> user = session.getUserDetails();
        //String name="vinit";
        //String email="vinit@gmail.com";
        String name = user.get(UserSessionManager.KEY_NAME);
        String email = user.get(UserSessionManager.KEY_EMAIL);
//        Log.d("After Name", name);
//        Log.d("After Email", email);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        txtHeaderEmail=(TextView)header.findViewById(R.id.txtHeader_Email);
        txtHeaderName=(TextView)header.findViewById(R.id.txtHeader_name);
        txtHeaderName.setText(name);
        txtHeaderEmail.setText(email);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Log.d("After Back pressed",mTitle.toString());
            getSupportActionBar().setTitle(mTitle);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if( id == R.id.menu_search){
            Log.d("After Search Button Clicked","Working properly");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_us) {
            AboutUs home=new AboutUs();
            fm.beginTransaction()
                    .replace(R.id.fragmentcontainer,home)
                    .addToBackStack(getSupportFragmentManager().toString())

                    .commit();

        } else if (id == R.id.nav_eventviewer) {

        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_settings) {

        }else if (id == R.id.nav_add_event) {
            Add_Event home=new Add_Event();
            fm.beginTransaction()
                    .replace(R.id.fragmentcontainer,home)
                    .commit();

        } else if (id == R.id.nav_help) {
            Help home=new Help();
            fm.beginTransaction()
                    .replace(R.id.fragmentcontainer, home)
                    .addToBackStack(getSupportFragmentManager().toString())
                    .commit();

        } else if (id == R.id.nav_feedback) {
            Feedback_fragment home=new Feedback_fragment();
            fm.beginTransaction()
                    .replace(R.id.fragmentcontainer, home)
                    .commit();

        } else if (id == R.id.nav_logout) {
            alertDialogBuilder
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(HomeNavigation.this,Login.class);
                            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                            session.logoutUser();
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onSectionAttached(int i) {
        switch (i)
        {
            case 0:
                mTitle="Home";
                setTitle(mTitle);
                break;
            case 1:
                mTitle = "About us";
                setTitle(mTitle);
                break;
            case 2:
                mTitle="My Event Viewer";
                setTitle(mTitle);
                break;
            case 3:
                mTitle="Notification";
                setTitle(mTitle);
                break;
            case 4:
                mTitle="Settings";
                setTitle(mTitle);
                break;
            case 5:
                mTitle="Help";
                setTitle(mTitle);
                break;
            case 6:
                mTitle="Feedback";
                setTitle(mTitle);
                break;
            case 7:
                mTitle="Gallery";
                setTitle(mTitle);
                break;
            case 8:
                mTitle="Events";
                setTitle(mTitle);
                break;
            case 9:
                mTitle="Add Event";
                setTitle(mTitle);
                break;
        }
    }


}
