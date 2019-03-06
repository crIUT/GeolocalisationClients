package com.example.clementramond.geolocalisationclients.service;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.asynctask.SynchronisationBD;
import com.example.clementramond.geolocalisationclients.database.dao.GeolocToAddDAO;
import com.example.clementramond.geolocalisationclients.modele.Droit;
import com.example.clementramond.geolocalisationclients.modele.Geolocalisation;

import org.threeten.bp.LocalDateTime;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LocationService extends IntentService {

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Location lastKnownLocation;

    private boolean geolocState;
    private boolean demarrage = true;

    private int permission;

    private GeolocToAddDAO geolocToAddDAO;

    /**
     * A constructor is required, and must call the super <code><a href="/reference/android/app/IntentService.html#IntentService(java.lang.String)">IntentService(String)</a></code>
     * constructor with a name for the worker thread.
     */
    public LocationService() {
        super("LocationService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        geolocToAddDAO = new GeolocToAddDAO(getApplicationContext());

        geolocState = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE).getBoolean(Params.PREF_GEOLOC, false);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (geolocState) {
            requestLocationUpdates();
        }

        return super.onStartCommand(intent,flags,startId);
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        boolean glPref;
        final SharedPreferences preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Geolocalisation> geolocs;
                while (true) {
                    try {
                        Thread.sleep(60000);
                        geolocs = geolocToAddDAO.getAll();
                        for (Geolocalisation geoloc : geolocs) {
                            SynchronisationBD.sendRequest(preferences, "type=INSERT"
                                        + "&pseudoUtilisateur="+Params.encode(geoloc.getUtilisateur().getPseudo())
                                        + "&date_heure="+Params.encode(geoloc.getDateTime().format(Geolocalisation.MYSQL_DTF))
                                        + "&lat="+Params.encode(String.valueOf(geoloc.getLatitude()))
                                        + "&lon="+Params.encode(String.valueOf(geoloc.getLongitude())));
                            if (SynchronisationBD.responseCode != 500 && SynchronisationBD.responseCode != -404) {
                                geolocToAddDAO.delete(geoloc);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
        try {
            while (true) {
                Thread.sleep(3000);
                glPref = preferences.getBoolean(Params.PREF_GEOLOC, false);
                if (glPref) {
                    if (!geolocState || demarrage) {
                        geolocState = true;
                        demarrage = false;
                        requestLocationUpdates();
                    }
                    getPosition();
                } else {
                    if (geolocState) {
                        geolocState = false;
                        permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permission == PermissionChecker.PERMISSION_GRANTED) {
                            locationManager.removeUpdates(locationListener);
                        }
                    }
                }
            }

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    private void requestLocationUpdates() {
        permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PermissionChecker.PERMISSION_GRANTED && locationManager != null) {
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void getPosition() {
        permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationManager != null && permission == PermissionChecker.PERMISSION_GRANTED) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null && Params.connectedUser != null
                    && !Params.connectedUser.getDroit().getDroit().equals(Droit.DROITS[2])) {
                Geolocalisation geoloc = new Geolocalisation();
                geoloc.setDateTime(LocalDateTime.now());
                geoloc.setUtilisateur(Params.connectedUser);
                geoloc.setLatitude(lastKnownLocation.getLatitude());
                geoloc.setLongitude(lastKnownLocation.getLongitude());
                geolocToAddDAO.save(geoloc);
            }
        }

    }

}
