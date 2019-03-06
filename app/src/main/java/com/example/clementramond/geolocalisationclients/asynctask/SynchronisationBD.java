package com.example.clementramond.geolocalisationclients.asynctask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.clementramond.geolocalisationclients.MD5;
import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.activity.OptionsActivity;
import com.example.clementramond.geolocalisationclients.database.dao.CategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.ClientDAO;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.database.dao.DroitDAO;
import com.example.clementramond.geolocalisationclients.database.dao.GeolocClientsDBDAO;
import com.example.clementramond.geolocalisationclients.database.dao.GeolocDAO;
import com.example.clementramond.geolocalisationclients.database.dao.SousCategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.UtilisateurDAO;
import com.example.clementramond.geolocalisationclients.modele.Categorie;
import com.example.clementramond.geolocalisationclients.modele.Client;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Droit;
import com.example.clementramond.geolocalisationclients.modele.Geolocalisation;
import com.example.clementramond.geolocalisationclients.modele.SousCategorie;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

import org.threeten.bp.LocalDateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class SynchronisationBD extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG_LOG = "ACCES WEB";

    private OptionsActivity activiteParente;

    private static SharedPreferences preferences;
    
    public static int responseCode;

    private ArrayList<Dossier> dossiers;
    private ArrayList<Droit> droits;
    private ArrayList<Utilisateur> utilisateurs;
    private ArrayList<Categorie> categories;
    private HashMap<Integer, SousCategorie> sousCategories;
    private ArrayList<Client> clients;
    private ArrayList<Geolocalisation> geolocs;

    public SynchronisationBD(OptionsActivity parent) {
        activiteParente = parent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activiteParente.loading(true);
        preferences = activiteParente.preferences;
    }

    @Override
    protected Boolean doInBackground(String... args) {

        String resultat = sendRequest("type=SELECT&nomTable=dossier");
        if (responseCode == 500) return false;
        dossiers = csvToListeDossier(responseToCsv(resultat));

        resultat = sendRequest("type=SELECT&nomTable=droit");
        if (responseCode == 500) return false;
        droits = csvToListeDroit(responseToCsv(resultat));

        resultat = sendRequest("type=SELECT&nomTable=utilisateur");
        if (responseCode == 500) return false;
        utilisateurs = csvToListeUtilisateur(responseToCsv(resultat));

        if (Params.dossier != null) {
            String prefix = String.format("%03d", Params.dossier.getId()) + "_";

            resultat = sendRequest("type=SELECT&nomTable=" + prefix + "categorie");
            if (responseCode == 500) return false;
            categories = csvToListeCategorie(responseToCsv(resultat));

            resultat = sendRequest("type=SELECT&nomTable=" + prefix + "sous_categorie");
            if (responseCode == 500) return false;
            sousCategories = csvToListeSousCategorie(responseToCsv(resultat));

            resultat = sendRequest("type=SELECT&nomTable=" + prefix + "client");
            if (responseCode == 500) return false;
            clients = csvToListeClient(responseToCsv(resultat));

            resultat = sendRequest("type=SELECT&nomTable=" + prefix + "geoloc");
            if (responseCode == 500) return false;
            geolocs = csvToListeGeoloc(responseToCsv(resultat));
        }

        new GeolocClientsDBDAO(activiteParente).deleteTablesContent();

        DossierDAO dossierDAO = new DossierDAO(activiteParente);
        for (Dossier dossier : dossiers) {
            dossierDAO.save(dossier);
        }

        DroitDAO droitDAO = new DroitDAO(activiteParente);
        for (Droit droit : droits) {
            droitDAO.save(droit);
        }

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(activiteParente);
        for (Utilisateur utilisateur : utilisateurs) {
            utilisateurDAO.save(utilisateur);
        }

        if (categories != null) {
            CategorieDAO categorieDAO = new CategorieDAO(activiteParente);
            for (Categorie categorie : categories) {
                categorieDAO.save(categorie);
            }
        }

        if (sousCategories != null) {
            SousCategorieDAO sousCategorieDAO = new SousCategorieDAO(activiteParente);
            ArrayList<SousCategorie> list = hashMapToArrayListSousCategorie(sousCategories);
            for (SousCategorie sousCategorie : list) {
                sousCategorieDAO.save(sousCategorie);
            }
        }

        if (clients != null) {
            ClientDAO clientDAO = new ClientDAO(activiteParente);
            for (Client client : clients) {
                clientDAO.save(client);
            }
        }

        if (geolocs != null) {
            GeolocDAO geolocDAO = new GeolocDAO(activiteParente);
            for (Geolocalisation geoloc : geolocs) {
                geolocDAO.save(geoloc);
            }
        }

        return true;
    }


    @Override
    protected void onPostExecute(Boolean resultat) {
        activiteParente.loading(false);
        if (!resultat) {
            Toast.makeText(activiteParente, "La synchronisation n'a pas été effectuée.",
                    Toast.LENGTH_SHORT).show();
        }
        activiteParente.refreshData();

        super.onPostExecute(resultat);
    }

    public String sendRequest(String requestParams) {
        return sendRequest(activiteParente.preferences
                .getString(Params.PREF_SERVER, Params.DEFAULT_SERVER), requestParams);
    }

    public static String sendRequest(SharedPreferences preferences, String requestParams) {
        return sendRequest(preferences
                .getString(Params.PREF_SERVER, Params.DEFAULT_SERVER), requestParams);
    }

    private static String sendRequest(String serverURL, String requestParams) {
        StringBuilder resultat = new StringBuilder();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Define the server endpoint to send the HTTP request to
            String requestURL = serverURL + "/apiBD.php";
            URL url = new URL(requestURL);
            urlConnection = (HttpURLConnection)url.openConnection();

            // Indicate that we want to write to the HTTP request body
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            // Writing the post data to the HTTP request body
            BufferedWriter httpRequestBodyWriter =
                    new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            httpRequestBodyWriter.write(requestParams+"&mdp="+ Params.encode(MD5.getMd5("X+O%ih4fGt&@5s0t")));
            httpRequestBodyWriter.close();

            responseCode = urlConnection.getResponseCode();

            reader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
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
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.i(TAG_LOG, "problème fermeture flux de lecture");
            }
        }

        return resultat.toString();
    }

    private ArrayList<ArrayList<String>> responseToCsv(String csv) {
        ArrayList<ArrayList<String>> lignesColonnes = new ArrayList<>();

        if (!csv.isEmpty()) {
            String[] lignes = csv.split("\n");
            String[] colonnes;
            ArrayList<String> nouvLigne;

            for (String ligne : lignes) {
                colonnes = ligne.split(";", 10);
                nouvLigne = new ArrayList<>();
                for (String colonne : colonnes) {
                    nouvLigne.add(colonne);
                }
                lignesColonnes.add(nouvLigne);
            }
        }
        return lignesColonnes;
    }

    private ArrayList<SousCategorie> hashMapToArrayListSousCategorie(HashMap<Integer, SousCategorie> hashMap) {
        ArrayList<SousCategorie> arrayList = new ArrayList<>();

        Set cles = hashMap.keySet();
        Iterator it = cles.iterator();
        while (it.hasNext()){
            Object cle = it.next();
            arrayList.add(hashMap.get(cle));
        }

        return arrayList;
    }

    private ArrayList<Dossier> csvToListeDossier(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<Dossier> listeDossier = new ArrayList<>();
        Dossier dossier;
        for (ArrayList<String> colonnes : arrayLists) {
            dossier = new Dossier();
            dossier.setId(Integer.parseInt(colonnes.get(0)));
            dossier.setNom(colonnes.get(1));
            listeDossier.add(dossier);
        }
        return listeDossier;
    }

    private ArrayList<Droit> csvToListeDroit(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<Droit> listeDroit = new ArrayList<>();
        Droit droit;
        for (ArrayList<String> colonnes : arrayLists) {
            droit = new Droit(colonnes.get(0));
            listeDroit.add(droit);
        }
        return listeDroit;
    }

    private ArrayList<Utilisateur> csvToListeUtilisateur(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<>();
        Utilisateur utilisateur;

        for (ArrayList<String> colonnes : arrayLists) {
            utilisateur = new Utilisateur();

            utilisateur.setPseudo(colonnes.get(0));
            utilisateur.setNom(colonnes.get(1));
            utilisateur.setPrenom(colonnes.get(2));
            utilisateur.setMdp(colonnes.get(3));

            if (colonnes.get(4).isEmpty()) {
                utilisateur.setDossier(null);
            } else {
                for (Dossier dossier : dossiers) {
                    if (dossier != null && dossier.getId() == Integer.parseInt(colonnes.get(4))) {
                        utilisateur.setDossier(dossier);
                        break;
                    }
                }
            }

            for (Droit droit : droits) {
                if (droit != null && droit.getDroit().equals(colonnes.get(5))) {
                    utilisateur.setDroit(droit);
                    break;
                }
            }

            listeUtilisateur.add(utilisateur);
        }
        return listeUtilisateur;
    }

    private ArrayList<Categorie> csvToListeCategorie(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<Categorie> listeCategorie = new ArrayList<>();
        Categorie categorie;
        for (ArrayList<String> colonnes : arrayLists) {
            categorie = new Categorie(colonnes.get(0));
            listeCategorie.add(categorie);
        }
        return listeCategorie;
    }

    private HashMap<Integer, SousCategorie> csvToListeSousCategorie(ArrayList<ArrayList<String>> arrayLists) {
        HashMap<Integer, SousCategorie> listeSousCategorie = new HashMap<>();
        SousCategorie sousCategorie;
        for (ArrayList<String> colonnes : arrayLists) {
            sousCategorie = new SousCategorie();

            sousCategorie.setNom(colonnes.get(1));

            for (Categorie categorie : categories) {
                if (categorie != null && categorie.getNom().equals(colonnes.get(2))) {
                    sousCategorie.setCategorie(categorie);
                    break;
                }
            }

            listeSousCategorie.put(Integer.parseInt(colonnes.get(0)), sousCategorie);
        }
        return listeSousCategorie;
    }

    private ArrayList<Client> csvToListeClient(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<Client> listeClient = new ArrayList<>();
        Client client;
        String lat;
        String lon;
        for (ArrayList<String> colonnes : arrayLists) {
            client = new Client();

            client.setId(Integer.parseInt(colonnes.get(0)));
            client.setSousCategorie(sousCategories.get(Integer.parseInt(colonnes.get(1))));
            client.setNom(colonnes.get(2));
            client.setPrenom(colonnes.get(3));
            client.setCodePostal(colonnes.get(4));

            lat = colonnes.get(5);
            if (lat.isEmpty()) {
                client.setLatitude(null);
            } else {
                client.setLatitude(Double.parseDouble(lat));
            }
            lon = colonnes.get(6);
            if (lon.isEmpty()) {
                client.setLongitude(null);
            } else {
                client.setLongitude(Double.parseDouble(lon));
            }

            client.setTelephonePortable(colonnes.get(7));
            client.setTelephoneFixe(colonnes.get(8));

            listeClient.add(client);
        }

        return listeClient;
    }

    private ArrayList<Geolocalisation> csvToListeGeoloc(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<Geolocalisation> listeGeolocalisation = new ArrayList<>();
        Geolocalisation geoloc;
        String lat;
        String lon;
        for (ArrayList<String> colonnes : arrayLists) {
            geoloc = new Geolocalisation();

            geoloc.setDateTime(LocalDateTime.parse(colonnes.get(0), Geolocalisation.MYSQL_DTF));

            for (Utilisateur utilisateur : utilisateurs) {
                if (utilisateur.getPseudo().equals(colonnes.get(1))) {
                    geoloc.setUtilisateur(utilisateur);
                    break;
                }
            }

            lat = colonnes.get(2);
            if (lat.isEmpty()) {
                geoloc.setLatitude(null);
            } else {
                geoloc.setLatitude(Double.parseDouble(lat));
            }
            lon = colonnes.get(3);
            if (lon.isEmpty()) {
                geoloc.setLongitude(null);
            } else {
                geoloc.setLongitude(Double.parseDouble(lon));
            }

            listeGeolocalisation.add(geoloc);
        }
        return listeGeolocalisation;
    }
}
