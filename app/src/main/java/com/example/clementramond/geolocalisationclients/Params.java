package com.example.clementramond.geolocalisationclients;

import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

public class Params {

    public final static int REQ_ACCESS_LOCATION = 0;

    public final static String PREFS = "prefs";
    public final static String PREF_GEOLOC = "geoloc";
    public final static String PREF_USER = "user";
    public final static String PREF_DOSSIER = "dossier";

    public static Utilisateur connectedUser = null;
    public static Dossier dossier = null;

}
