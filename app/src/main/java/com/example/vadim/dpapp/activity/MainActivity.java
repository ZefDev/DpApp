package com.example.vadim.dpapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.fragments.DocFragment;
import com.example.vadim.dpapp.fragments.TaskFragment;
import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.fragments.ActivFragment;
import com.example.vadim.dpapp.containers.TaskContainer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<TaskContainer> tasks;
    DBHelper dbHelper;
    Intent intent;
    Intent Auth;
    TaskFragment taskFragment;
    ActivFragment activFragment;
    FloatingActionButton fab;

//    private void init () {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Auth = getIntent();
        taskFragment = new TaskFragment();
        activFragment = new ActivFragment();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        if(AppConfig.rights.equals ("Контрагент")) {
            nav_Menu.findItem(R.id.tasks).setVisible(false);
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
            nav_Menu.findItem(R.id.documents).setVisible(false);
            nav_Menu.findItem(R.id.nav_update).setVisible(false);
            fab.setVisibility(View.INVISIBLE);
        }
        AppConfig.flagEnter = true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        TextView textLogin = (TextView) findViewById(R.id.textLogin);
        TextView textContractor = (TextView) findViewById(R.id.textView);
        textLogin.setText(AppConfig.User);
        textContractor.setText(AppConfig.Contractor);
        ImageView ico = (ImageView) findViewById(R.id.imageView);
        if(AppConfig.rights.equals("Контрагент")) {
            ico.setImageResource(R.drawable.dp_ico_contractor);
        }
        else if(AppConfig.rights.equals("Техник")){
            ico.setImageResource(R.drawable.dp_ico_tecnick);
        }
        else {
            ico.setImageResource(R.drawable.dp_ico_admin);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            dbHelper.deleteAll();
        }
        else if (id == R.id.action_setting) {
            Intent setting = new Intent(MainActivity.this, Setting.class);
            startActivity(setting);
        }
        else if(id == R.id.action_filter){
            Intent filter= new Intent(MainActivity.this,)
        }
        return super.onOptionsItemSelected(item);
    }

    Fragment fragment = null;
    String currFragment = "";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tasks) {
            runFragment("TaskFragment");
            fab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.activs) {
            runFragment("ActivFragment");
            if (!AppConfig.rights.equals("Контрагент")) {
                fab.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.documents) {
            runFragment("DocFragment");
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_update) {
            intent = new Intent(MainActivity.this, Pizdec.class);
            startActivity(intent);
        }
        else if (id == R.id.exit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            else {
                ((AppCompatActivity) this).finish();
            }
        }
        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void runFragment(String currFragment){
        this.currFragment = currFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (currFragment){
            case "TaskFragment":
                fragment = new TaskFragment();
                currFragment = "TaskFragment";
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.commit();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            intent = new Intent(MainActivity.this, TaskActivity.class);

                        intent.putExtra("flag",true);
                        startActivity(intent);
                    }
                    // }
                });
                break;
            case "ActivFragment":
                fragment = new ActivFragment();
                currFragment = "ActivFragment";
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.commit();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(AppConfig.rights.equals("Контрагент")){
                            intent = new Intent(MainActivity.this, OtchetActivity.class);
                        }
                        else {
                            intent = new Intent(MainActivity.this, ElementActivity.class);
                        }
                        intent.putExtra("flag",true);
                        startActivity(intent);
                    }
                    // }
                });
                break;
            case "DocFragment":
                fragment = new DocFragment();
                currFragment = "DocFragment";
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.commit();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(MainActivity.this, DocActivity.class);
                        intent.putExtra("flag",true);
                        intent.putExtra("isEdit","false");
                        startActivity(intent);
                    }
                    // }
                });
                break;
        }
    }

    @Override
    protected void onResume() {
        //runFragment(currFragment);
        super.onResume();
    }

    protected void onStart(){
        super.onStart();
        //runFragment("ActivFragment");
    }
}
