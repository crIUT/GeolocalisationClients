package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.database.dao.CategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.ClientDAO;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.database.dao.DroitDAO;
import com.example.clementramond.geolocalisationclients.database.dao.GeolocDAO;
import com.example.clementramond.geolocalisationclients.database.dao.SousCategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.UtilisateurDAO;
import com.example.clementramond.geolocalisationclients.modele.Client;
import com.example.clementramond.geolocalisationclients.service.LocationService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private Switch geolocSwitch;

    private ListView list;

    private Spinner tables;

    private ComponentName locationServiceComponentName;
    private ComponentName serviceComponentName;

    private SharedPreferences preferences;

    private ArrayAdapter<Object> adapteurObject;
    private SpinnerAdapter adapteurTable;
    private ArrayList<Object> objectsFromCursor;

    private static final String[] TABLES = new String[] {
        "Dossiers",
        "Droits",
        "Utilisateurs",
        "Catégories",
        "Sous-catégories",
        "Clients",
        "Géolocalisations"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectsFromCursor = new ArrayList<>();

        list = findViewById(R.id.list);
        adapteurObject = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, objectsFromCursor);
        list.setAdapter(adapteurObject);
        list.setOnItemClickListener(this);

        tables = findViewById(R.id.tables);
        adapteurTable = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TABLES);

        tables.setAdapter(adapteurTable);
        tables.setOnItemSelectedListener(this);

        preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);

        geolocSwitch = findViewById(R.id.geolocSwitch);
        geolocSwitch.setChecked(preferences.getBoolean(Params.PREF_GEOLOC, true));

        geolocSwitch.setOnCheckedChangeListener(this);

        locationServiceComponentName = new ComponentName(this, LocationService.class);

        Params.connectedUser = new UtilisateurDAO(this).getAll().get(0);

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                DossierDAO dossierDAO = new DossierDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(dossierDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
            case 1:
                DroitDAO droitDAO = new DroitDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(droitDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
            case 2:
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(utilisateurDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
            case 3:
                CategorieDAO categorieDAO = new CategorieDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(categorieDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
            case 4:
                SousCategorieDAO sousCategorieDAO = new SousCategorieDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(sousCategorieDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
            case 5:
                ClientDAO clientDAO = new ClientDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(clientDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
            case 6:
                GeolocDAO geolocDAO = new GeolocDAO(this);
                objectsFromCursor.clear();
                objectsFromCursor.addAll(geolocDAO.getAll());
                adapteurObject.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
            case R.id.list:
                if (tables.getSelectedItem().equals(TABLES[5])) {
                    Client client = (Client) objectsFromCursor.get(i);
                    // Appel de GoogleMap
                    Double  lat = client.getLatitude(),
                        lon = client.getLongitude();
                    String position = client.getCodePostal();
                    if (!(lat == Double.POSITIVE_INFINITY
                        || lon == Double.POSITIVE_INFINITY)) {
                        position = lat+","+lon;
                    }
                    String uri = "google.navigation:"+"q="+position;
                    Uri gmmIntentUri = Uri.parse(uri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(this, "Google Maps n'est pas disponible", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
