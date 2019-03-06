package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.asynctask.SynchronisationBD;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.database.dao.UtilisateurDAO;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.service.LocationService;

public abstract class OptionsActivity extends LoadingActivity {

    public SharedPreferences preferences;

    public boolean isLoginActivity = false;

    private boolean syncRequired = false;

    private ComponentName locationServiceComponentName;
    private ComponentName serviceComponentName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationServiceComponentName = new ComponentName(this, LocationService.class);
        preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);
        if (!preferences.contains(Params.PREF_GEOLOC)) {
            preferences.edit().putBoolean(Params.PREF_GEOLOC, true).apply();
        }
        if (!preferences.contains(Params.PREF_SERVER)) {
            preferences.edit().putString(Params.PREF_SERVER, Params.DEFAULT_SERVER).apply();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startLocationService();
        if (syncRequired) {
            synchroniser();
            syncRequired = false;
        }
        if (!isLoginActivity) {
            Dossier dossierUser;
            if (Params.connectedUser == null) {
                connexion();
                dossierUser = null;
            } else {
                dossierUser = Params.connectedUser.getDossier();
            }

            if (Params.dossier == null && dossierUser != null) {
                Params.dossier = dossierUser;
            } else {
                String idDossier = preferences.getString(Params.PREF_DOSSIER, null);
                Params.dossier =
                        (idDossier == null) ? null : new DossierDAO(this).getFromId(idDossier);
                if (Params.dossier == null) {
                    connexion();
                }
            }
        }
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
                startActivityForResult(intent, Params.REQ_SETTINGS);
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
            .putString(Params.PREF_DOSSIER, null)
            .apply();
        connexion();
    }

    public void connexion() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, Params.REQ_CONNEXION);
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

    public abstract void refreshData();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Params.REQ_CONNEXION :
                if (resultCode == RESULT_OK) {
                    syncRequired = true;
                }
                break;
            case Params.REQ_SETTINGS :
                if (resultCode == Params.RESULT_SYNC) {
                    synchroniser();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Params.REQ_ACCESS) {
            for (int i=0 ; i < permissions.length ; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PermissionChecker.PERMISSION_GRANTED) {
                        startLocationService();
                    } else {
                        preferences.edit().putBoolean(Params.PREF_GEOLOC, false).apply();
                    }
                }
            }
        }
    }

    public void startLocationService() {
        int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PermissionChecker.PERMISSION_GRANTED && preferences.getBoolean(Params.PREF_GEOLOC, true)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Params.REQ_ACCESS
            );
        } else if (!Params.serviceStarted && (serviceComponentName == null || locationServiceComponentName != null
                && !(locationServiceComponentName.getClassName().equals(serviceComponentName.getClassName())))
                && preferences.getBoolean(Params.PREF_GEOLOC, false)) {
            serviceComponentName = startService(new Intent(this, LocationService.class));
            Params.serviceStarted = true;
        }
    }
}
