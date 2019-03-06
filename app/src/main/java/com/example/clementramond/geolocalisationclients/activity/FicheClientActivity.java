package com.example.clementramond.geolocalisationclients.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.asynctask.SynchronisationBD;
import com.example.clementramond.geolocalisationclients.database.dao.ClientDAO;
import com.example.clementramond.geolocalisationclients.modele.Client;
import com.example.clementramond.geolocalisationclients.modele.Geolocalisation;

public class FicheClientActivity extends OptionsActivity {

    private Client client;

    private TextView mNomView, mPrenomView, mCategorieView, mCodePostalView,
            mTelFixeView, mTelPortableView, mLatitudeView, mLongitudeView;

    private Location lastKnownLocation = null;

    private ClientDAO clientDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_client);

        super.setActivity(R.id.activity);
        super.setLoading(R.id.loading);

        clientDAO = new ClientDAO(this);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastKnownLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        mNomView = findViewById(R.id.nom);
        mPrenomView = findViewById(R.id.prenom);
        mCategorieView = findViewById(R.id.categorie);
        mCodePostalView = findViewById(R.id.code_postal);
        mTelFixeView = findViewById(R.id.fixe);
        mTelPortableView = findViewById(R.id.portable);
        mLatitudeView = findViewById(R.id.latitude);
        mLongitudeView = findViewById(R.id.longitude);

        client = getIntent().getExtras().getParcelable(Params.EXT_CLIENT);

        setViews();
    }

    @Override
    public void refreshData() {
        clientDAO = new ClientDAO(this);
        client = clientDAO.getFromId(client.getId());
        setViews();
    }

    private void setViews() {
        mNomView.setText(client.getNom());
        mPrenomView.setText(client.getPrenom());
        mCategorieView.setText(client.getSousCategorie().getCategorie().getNom()
                + " - " + client.getSousCategorie().getNom());
        mCodePostalView.setText(client.getCodePostal());
        mTelFixeView.setText(client.getTelephoneFixe());
        mTelPortableView.setText(client.getTelephonePortable());
        mLatitudeView.setText(client.getLatitude() == null ? "inconnue" : client.getLatitude() + "");
        mLongitudeView.setText(client.getLongitude() == null ? "inconnue" : client.getLongitude() + "");
    }

    public void onClickItineraire(View view) {
        // Appel de GoogleMap
        Double lat = client.getLatitude(),
                lon = client.getLongitude();
        String position = client.getCodePostal();
        if (!(lat == null
                || lon == null)) {
            position = lat + "," + lon;
        }
        String uri = "google.navigation:" + "q=" + position;
        Uri gmmIntentUri = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps n'est pas disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCoordonnees(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Positionner le client")
                .setMessage("Souhaitez-vous que votre position actuelle soit enregistrée comme coordonnées de " + client + " ?")
                .setNegativeButton("Non", null)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setCoordonnees();
                    }
                })
                .show();
    }

    public void setCoordonnees() {
        final Toast TOAST_RESULT = Toast.makeText(getApplicationContext(), "Les coordonnées ont bien été enregistrées.", Toast.LENGTH_SHORT);
        if (lastKnownLocation != null) {
            client.setLatitude(lastKnownLocation.getLatitude());
            client.setLongitude(lastKnownLocation.getLongitude());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SynchronisationBD.sendRequest(preferences, "type=UPDATE"
                                    + "&dossier="+Params.encode(String.valueOf(Params.dossier.getId()))
                                    + "&idClient="+Params.encode(String.valueOf(client.getId()))
                                    + "&lat="+Params.encode(String.valueOf(client.getLatitude()))
                                    + "&lon="+Params.encode(String.valueOf(client.getLongitude())));
                    if (SynchronisationBD.responseCode == 500) {
                        TOAST_RESULT.setText("Les coordonnées n'ont pas été enregistrées :\n" +
                                "Le serveur a rencontré un problème.");
                        TOAST_RESULT.show();
                    } else if (SynchronisationBD.responseCode == -404) {
                        TOAST_RESULT.setText("Les coordonnées n'ont pas été enregistrées :\n" +
                                "Vérifiez votre connexion internet.");
                        TOAST_RESULT.show();
                    } else {
                        clientDAO.update(client);
                        activity.post(new Runnable() {
                            @Override
                            public void run() {
                                setViews();
                            }
                        });
                        setResult(Activity.RESULT_OK);
                        TOAST_RESULT.show();
                    }
                }
            }).start();
        } else {
            TOAST_RESULT.show();
        }
    }
}
