package com.example.simar.soundboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.simar.soundboard.Fragments.BoardFragment;
import com.example.simar.soundboard.Fragments.NewAudioFragment;
import com.example.simar.soundboard.Fragments.RecordingsFragment;
import com.example.simar.soundboard.Fragments.SettingsFragment;
import com.example.simar.soundboard.soundboard.Recording;
import com.example.simar.soundboard.soundboard.Recordings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


//TODO: alays remember to fucking implement the OnFragmentInteractionListner to the main Method
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnFragmentInteractionListener,
        RecordingsFragment.OnFragmentInteractionListener, NewAudioFragment.OnFragmentInteractionListener,
        BoardFragment.OnFragmentInteractionListener {

    public static boolean writePermission = false;
    public static boolean recordPermission = false;

    public static final Type REVIEW_TYPE = new TypeToken<List<Recordings>>() {}.getType();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Recordings recordings = jsonFromFile();
        recordings.validateRecordings();
        //TODO Overload the method in the Recordings class to return Recordings object and overload again to return arraylist of recordings type
        jsonToFile(jsonFromFile().validateRecordings());

    }

    @Override
    public void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}
                    , 1);
            MainActivity.recordPermission = true;

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                recordPermission = false;
                MainActivity.recordPermission = true;
            } else {
                MainActivity.recordPermission = false;
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 1);
            MainActivity.writePermission = true;

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writePermission = false;
                MainActivity.writePermission = true;
            } else {
                MainActivity.writePermission = false;
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Fragment objects
        BoardFragment boardFragment = new BoardFragment();
        RecordingsFragment recordingsFragment = new RecordingsFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        //Fragment Manager object
        FragmentManager fragmentManager;
        FragmentTransaction transition;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //If clicked, commence fragment switching
        if (id == R.id.nav_soundboard_fragment) {

            fragmentManager = getSupportFragmentManager();
            transition = fragmentManager.beginTransaction();
            transition.replace(R.id.contentFragment, boardFragment);
            transition.commit();

        } else if (id == R.id.nav_recording_fragment) {


            fragmentManager = getSupportFragmentManager();
            transition = fragmentManager.beginTransaction();
            transition.replace(R.id.contentFragment, recordingsFragment);
            transition.commit();

        } else if (id == R.id.nav_settings_fragment) {

            fragmentManager = getSupportFragmentManager();
            transition = fragmentManager.beginTransaction();
            transition.replace(R.id.contentFragment, settingsFragment);
            transition.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static void jsonToFile(Recordings recordings){
        try (FileWriter file = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recordings.json")) {
            file.write((new Gson()).toJson(recordings).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Recordings jsonFromFile(){
        Recordings recordings = new Recordings();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recordings.json"));
            recordings = (new Gson()).fromJson(reader , Recordings.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(recordings==null){
            return new Recordings();
        }
        return recordings;
    }
}
