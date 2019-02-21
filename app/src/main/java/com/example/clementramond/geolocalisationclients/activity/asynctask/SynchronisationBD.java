package com.example.clementramond.geolocalisationclients.activity.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.clementramond.geolocalisationclients.activity.LoadingActivity;

public class SynchronisationBD extends AsyncTask<String, Integer, Boolean> {

    private LoadingActivity activiteParente;

    public SynchronisationBD(LoadingActivity parent) {
        activiteParente = parent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activiteParente.loading(true);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        // Simulate network access.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        activiteParente.loading(false);
    }
}
