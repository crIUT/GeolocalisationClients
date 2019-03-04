package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.service.LocationService;

import java.util.ArrayList;

public class MainActivity extends OptionsActivity implements  AdapterView.OnItemSelectedListener,
                                                                AdapterView.OnItemClickListener {

    private ListView lignesTable;

    private Spinner spinnerTables;

    private ComponentName locationServiceComponentName;
    private ComponentName serviceComponentName;

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

        super.setActivity(R.id.activity);
        super.setLoading(R.id.loading);

        objectsFromCursor = new ArrayList<>();

        lignesTable = findViewById(R.id.lignesTable);
        adapteurObject = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, objectsFromCursor);
        lignesTable.setAdapter(adapteurObject);
        lignesTable.setOnItemClickListener(this);

        spinnerTables = findViewById(R.id.spinnerTables);
        adapteurTable = new ArrayAdapter<>(this, R.layout.custom_dropdown_item, TABLES);

        spinnerTables.setAdapter(adapteurTable);
        spinnerTables.setOnItemSelectedListener(this);

        locationServiceComponentName = new ComponentName(this, LocationService.class);

        int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PermissionChecker.PERMISSION_GRANTED && preferences.getBoolean(Params.PREF_GEOLOC, true)) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Params.REQ_ACCESS
            );
        }

        if (serviceComponentName == null || locationServiceComponentName != null
                && !(locationServiceComponentName.getClassName().equals(serviceComponentName.getClassName()))) {
            serviceComponentName = startService(new Intent(this, LocationService.class));
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (Params.connectedUser == null) {
            String pseudo = preferences.getString(Params.PREF_USER, null);
            Params.connectedUser =
                (pseudo==null) ? null : new UtilisateurDAO(this).getFromPseudo(pseudo);
            if (Params.connectedUser == null) {
                connexion();
                return;
            }
        }

        Dossier dossierUser = Params.connectedUser.getDossier();
        if (Params.dossier == null && dossierUser != null) {
            Params.dossier = dossierUser;
        } else {
            String idDossier = preferences.getString(Params.PREF_DOSSIER, null);
            Params.dossier =
                (idDossier==null) ? null : new DossierDAO(this).getFromId(idDossier);
            if (Params.dossier == null) {
                connexion();
            }
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
        switch (adapterView.getId()) {
            case R.id.lignesTable:
                if (spinnerTables.getSelectedItem().equals(TABLES[5])) {
                    Client client = (Client) objectsFromCursor.get(i);
                    // Appel de GoogleMap
                    Double  lat = client.getLatitude(),
                        lon = client.getLongitude();
                    String position = client.getCodePostal();
                    if (!(lat == null
                        || lon == null)) {
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
