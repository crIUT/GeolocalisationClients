package com.example.clementramond.geolocalisationclients;

import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Params {

    public static final int REQ_ACCESS = 0;
    public static final int REQ_CONNEXION = 1;
    public static final int REQ_POS_CLIENT = 2;
    public static final int REQ_SETTINGS = 3;

    public static final int RESULT_SYNC = 1;

    public final static String PREFS = "prefs";
    public final static String PREF_GEOLOC = "geoloc";
    public final static String PREF_USER = "user";
    public final static String PREF_DOSSIER = "dossier";
    public final static String PREF_SERVER = "server";


    public final static String EXT_CLIENT = "client";

    public final static String DEFAULT_SERVER = "http://www.mmsplanning.com";

    public static Utilisateur connectedUser = null;
    public static Dossier dossier = null;

    public static boolean serviceStarted = false;

    public static String encode(String string) {
        try {
            return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }
}
