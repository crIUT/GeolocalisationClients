package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Droit;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private SharedPreferences preferences;

    private Switch mGeolocSwitch;
    private Spinner mDossierSpinner;

    private ArrayAdapter<Dossier> mDossierAdapter;

    private ArrayList<Dossier> dossiers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);

        mGeolocSwitch = findViewById(R.id.geolocSwitch);
        mGeolocSwitch.setChecked(preferences.getBoolean(Params.PREF_GEOLOC, true));

        mGeolocSwitch.setOnCheckedChangeListener(this);

        mDossierSpinner = findViewById(R.id.dossier);

        DossierDAO dossierDAO = new DossierDAO(this);
        String idDossier = preferences.getString(Params.PREF_DOSSIER, null);
        Dossier dossier = idDossier==null?null:dossierDAO.getFromId(idDossier);

        if (Params.connectedUser != null && dossier != null) {
            if (Params.connectedUser.getDroit().getDroit().equals(Droit.DROITS[2])) {
                dossiers = dossierDAO.getAll();
                mDossierSpinner.setEnabled(true);
            } else {
                mDossierSpinner.setEnabled(false);
                dossiers = new ArrayList<>();
                dossiers.add(dossier);
            }
            mDossierAdapter = new ArrayAdapter<>(this, R.layout.custom_dropdown_item, dossiers);

            mDossierSpinner.setAdapter(mDossierAdapter);
            mDossierSpinner.setOnItemSelectedListener(this);

            int i;
            boolean trouvee = false;
            for (i=0 ; !trouvee && i < dossiers.size() ; i++) {
                trouvee = dossier.getId() == dossiers.get(i).getId();
            } i--;
            mDossierSpinner.setSelection(i);
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
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Params.REQ_ACCESS
            );
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Params.dossier = dossiers.get(i);
        preferences.edit().putString(Params.PREF_DOSSIER, String.valueOf(Params.dossier.getId())).apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
