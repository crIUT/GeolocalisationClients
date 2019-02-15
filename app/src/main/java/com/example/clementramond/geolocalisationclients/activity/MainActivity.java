package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.service.LocationService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch geolocSwitch;

    private ListView dossiers;

    private ComponentName locationServiceComponentName;
    private ComponentName serviceComponentName;

    private SharedPreferences preferences;

    private DossierDAO accesDossiers;
    private ArrayAdapter<Dossier> adapteurDossier;
    private ArrayList<Dossier> dossiersFromCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accesDossiers = new DossierDAO(this);

        /* Initialisation de la liste de dossier */

        dossiers = findViewById(R.id.dossiers);

        dossiersFromCursor = accesDossiers.getAllDossiers();

        adapteurDossier = new ArrayAdapter<>(
            this, android.R.layout.simple_list_item_1, dossiersFromCursor
        );

        dossiers.setAdapter(adapteurDossier);

        /* ------------------------------------- */

        preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);

        geolocSwitch = findViewById(R.id.geolocSwitch);
        geolocSwitch.setChecked(preferences.getBoolean(Params.PREF_GEOLOC, true));

        geolocSwitch.setOnCheckedChangeListener(this);

        locationServiceComponentName = new ComponentName(this, LocationService.class);

        int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PermissionChecker.PERMISSION_GRANTED && preferences.getBoolean(Params.PREF_GEOLOC, false)) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Params.REQ_ACCESS_LOCATION
            );
        }

        if (serviceComponentName == null || locationServiceComponentName != null
                && !(locationServiceComponentName.getClassName().equals(serviceComponentName.getClassName()))) {
            serviceComponentName = startService(new Intent(this, LocationService.class));
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Params.PREF_GEOLOC, b);
        editor.apply();
        if (b && PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Params.REQ_ACCESS_LOCATION
            );
        }
    }
}
