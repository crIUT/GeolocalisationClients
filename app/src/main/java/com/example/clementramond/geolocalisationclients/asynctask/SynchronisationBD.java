package com.example.clementramond.geolocalisationclients.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.activity.LoadingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SynchronisationBD extends AsyncTask<String, Integer, StringBuilder> {

    private static final String TAG_LOG = "ACCES WEB";

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
    protected StringBuilder doInBackground(String... args) {

        StringBuilder resultat = new StringBuilder();

        HttpURLConnection connexion = null;
        BufferedReader reader = null;

        // Simulate network access.
        try {
            URL url = new URL(args[0]);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.connect();

            reader = new BufferedReader(
                    new InputStreamReader(connexion.getInputStream(), StandardCharsets.UTF_8));
            String chaineLue = "";
            while ((chaineLue = reader.readLine()) != null) {
                resultat.append(chaineLue+"\n");
            }
        } catch (MalformedURLException e) {
            // une chaîne vide sera renvoyée
            Log.i(TAG_LOG, "url mal formé");
        } catch (IOException e) {
            Log.i(TAG_LOG, "problème lecture réponse");
        } finally {
            if (connexion != null) {
                connexion.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.i(TAG_LOG, "problème fermeture flux de lecture");
            }
        }

        return resultat;
    }

    @Override
    protected void onPostExecute(StringBuilder resultat) {
        if (resultat.length() != 0) {
            try {
                JSONObject jsonResult = new JSONObject(resultat.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        activiteParente.loading(false);
        super.onPostExecute(resultat);
    }
}
