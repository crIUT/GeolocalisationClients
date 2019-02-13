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

public class LocationService extends IntentService {

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Location lastKnownLocation;

    private boolean geoloc;

    private int permission;

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

        geoloc = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE).getBoolean(Params.PREF_GEOLOC, false);

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

        if (geoloc) {
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
        SharedPreferences preferences = getSharedPreferences(Params.PREFS, Activity.MODE_PRIVATE);
        try {
            while (true) {
                Thread.sleep(3000);
                glPref = preferences.getBoolean(Params.PREF_GEOLOC, false);
                if (glPref) {
                    if (!geoloc) {
                        geoloc = true;
                        requestLocationUpdates();
                    }
                    getPosition();
                } else {
                    if (geoloc) {
                        geoloc = false;
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
            if (lastKnownLocation != null) {
                // TODO enregistrer la longitude et la latitude
                // lastKnownLocation.getLatitude();
                // lastKnownLocation.getLongitude();
            }
        }

    }

}
