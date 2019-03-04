package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.asynctask.SynchronisationBD;

public abstract class OptionsActivity extends LoadingActivity {

    public SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.parametres:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.sync:
                synchroniser();
                break;
            case R.id.deconnexion:
                deconnexion();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    public void deconnexion() {
        Params.connectedUser = null;
        Params.dossier = null;
        preferences.edit()
            .putString(Params.PREF_USER, null)
            .putString(Params.PREF_DOSSIER, null)
            .apply();
        connexion();
    }

    public void connexion() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void synchroniser() {
        int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permission != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, Params.REQ_ACCESS
            );
        } else {
            new SynchronisationBD(this).execute();
        }
    }

}
