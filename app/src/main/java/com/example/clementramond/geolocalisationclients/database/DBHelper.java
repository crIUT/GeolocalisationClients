package com.example.clementramond.geolocalisationclients.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.clementramond.geolocalisationclients.database.dao.CategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.ClientDAO;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.database.dao.DroitDAO;
import com.example.clementramond.geolocalisationclients.database.dao.SousCategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.UtilisateurDAO;
import com.example.clementramond.geolocalisationclients.modele.Categorie;
import com.example.clementramond.geolocalisationclients.modele.Client;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Droit;
import com.example.clementramond.geolocalisationclients.modele.SousCategorie;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // http://androidopentutorials.com/android-sqlite-join-multiple-tables-example/

    private static final String DATABASE_NAME = "geolocalisationclients.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DOSSIER = "dossier";
    public static final String TABLE_DROIT = "droit";
    public static final String TABLE_UTILISATEUR = "utilisateur";
    public static final String TABLE_CATEGORIE = "categorie";
    public static final String TABLE_SOUS_CATEGORIE = "sous_categorie";
    public static final String TABLE_CLIENT = "client";
    public static final String TABLE_GEOLOC = "geoloc";
    public static final String TABLE_GEOLOC_TO_ADD = "geoloc_to_add";

    public static final String[] DOSSIER_COLUMNS = new String[]{
        "d_id",
        "d_nom"
    };
    public static final int DOSSIER_ID = 0;
    public static final int DOSSIER_NOM = 1;

    public static final String[] DROIT_COLUMNS = new String[]{
        "droit"
    };
    public static final int DROIT_DROIT = 0;

    public static final String[] UTILISATEUR_COLUMNS = new String[]{
        "pseudo",
        "u_nom",
        "u_prenom",
        "mdp",
        "id_dossier",
        "u_droit"
    };
    public static final int UTILISATEUR_PSEUDO = 0;
    public static final int UTILISATEUR_NOM = 1;
    public static final int UTILISATEUR_PRENOM = 2;
    public static final int UTILISATEUR_MDP = 3;
    public static final int UTILISATEUR_ID_DOSSIER = 4;
    public static final int UTILISATEUR_DROIT = 5;

    public static final String[] CATEGORIE_COLUMNS = new String[]{
        "cat_nom"
    };
    public static final int CATEGORIE_NOM = 0;

    public static final String[] SOUS_CATEGORIE_COLUMNS = new String[]{
        "sc_nom",
        "sc_categorie"
    };
    public static final int SOUS_CATEGORIE_NOM = 0;
    public static final int SOUS_CATEGORIE_CATEGORIE = 1;

    public static final String[] CLIENT_COLUMNS = new String[]{
        "c_id",
        "c_categorie",
        "c_sous_categorie",
        "c_nom",
        "c_prenom",
        "cp",
        "tel_fixe",
        "tel_portable",
        "c_latitude",
        "c_longitude"
    };
    public static final int CLIENT_ID = 0;
    public static final int CLIENT_CATEGORIE = 1;
    public static final int CLIENT_SOUS_CATEGORIE = 2;
    public static final int CLIENT_NOM = 3;
    public static final int CLIENT_PRENOM = 4;
    public static final int CLIENT_CP = 5;
    public static final int CLIENT_TEL_FIXE = 6;
    public static final int CLIENT_TEL_PORTABLE = 7;
    public static final int CLIENT_LATITUDE = 8;
    public static final int CLIENT_LONGITUDE = 9;

    public static final String[] GEOLOC_COLUMNS = new String[]{
            "dateTime",
            "g_utilisateur",
            "g_latitude",
            "g_longitude"
    };
    public static final int GEOLOC_DATETIME = 0;
    public static final int GEOLOC_UTILISATEUR = 1;
    public static final int GEOLOC_LATITUDE = 2;
    public static final int GEOLOC_LONGITUDE = 3;

    public static final String[] GEOLOC_TO_ADD_COLUMNS = new String[]{
            "ta_dateTime",
            "ta_g_utilisateur",
            "ta_g_latitude",
            "ta_g_longitude"
    };
    public static final int GEOLOC_TO_ADD_DATETIME = 0;
    public static final int GEOLOC_TO_ADD_UTILISATEUR = 1;
    public static final int GEOLOC_TO_ADD_LATITUDE = 2;
    public static final int GEOLOC_TO_ADD_LONGITUDE = 3;

    private static final String CREATION_TABLE_DOSSIER =
        "CREATE TABLE " + TABLE_DOSSIER + " ( "
            + DOSSIER_COLUMNS[DOSSIER_ID] + " INTEGER PRIMARY KEY, "
            + DOSSIER_COLUMNS[DOSSIER_NOM] + " TEXT"
            +");";

    private static final String CREATION_TABLE_DROIT =
        "CREATE TABLE " + TABLE_DROIT+ " ( "
            + DROIT_COLUMNS[DROIT_DROIT] + " TEXT PRIMARY KEY"
            +");";

    private static final String CREATION_TABLE_UTILISATEUR =
        "CREATE TABLE " + TABLE_UTILISATEUR + " ( "
            + UTILISATEUR_COLUMNS[UTILISATEUR_PSEUDO] + " TEXT PRIMARY KEY, "
            + UTILISATEUR_COLUMNS[UTILISATEUR_NOM] + " TEXT, "
            + UTILISATEUR_COLUMNS[UTILISATEUR_PRENOM] + " TEXT, "
            + UTILISATEUR_COLUMNS[UTILISATEUR_MDP] + " TEXT,"
            + UTILISATEUR_COLUMNS[UTILISATEUR_ID_DOSSIER] + " INTEGER,"
            + UTILISATEUR_COLUMNS[UTILISATEUR_DROIT] + " TEXT, "
            + " FOREIGN KEY ("+ UTILISATEUR_COLUMNS[UTILISATEUR_ID_DOSSIER] +") REFERENCES "+ TABLE_DOSSIER +"("+DOSSIER_COLUMNS[DOSSIER_ID]+"),"
            + " FOREIGN KEY ("+ UTILISATEUR_COLUMNS[UTILISATEUR_DROIT] +") REFERENCES "+ TABLE_DROIT +"("+DROIT_COLUMNS[DROIT_DROIT]+")"
            +");";

    private static final String CREATION_TABLE_CATEGORIE =
        "CREATE TABLE " + TABLE_CATEGORIE + " ( "
            + CATEGORIE_COLUMNS[CATEGORIE_NOM] + " TEXT PRIMARY KEY"
            +");";

    private static final String CREATION_TABLE_SOUS_CATEGORIE =
        "CREATE TABLE " + TABLE_SOUS_CATEGORIE + " ( "
            + SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_NOM] + " TEXT, "
            + SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_CATEGORIE] + " TEXT, "
            + " FOREIGN KEY ("+ SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_CATEGORIE] +") REFERENCES "+ TABLE_CATEGORIE +"("+CATEGORIE_COLUMNS[CATEGORIE_NOM]+"),"
            + " PRIMARY KEY ("+ SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_NOM] +", "+SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_CATEGORIE]+")"
            +");";

    private static final String CREATION_TABLE_CLIENT =
        "CREATE TABLE " + TABLE_CLIENT + " ( "
            + CLIENT_COLUMNS[CLIENT_ID] + " INTEGER PRIMARY KEY, "
            + CLIENT_COLUMNS[CLIENT_CATEGORIE] + " TEXT, "
            + CLIENT_COLUMNS[CLIENT_SOUS_CATEGORIE] + " TEXT, "
            + CLIENT_COLUMNS[CLIENT_NOM]+ " TEXT, "
            + CLIENT_COLUMNS[CLIENT_PRENOM]+ " TEXT, "
            + CLIENT_COLUMNS[CLIENT_CP]+ " TEXT, "
            + CLIENT_COLUMNS[CLIENT_TEL_FIXE] + " TEXT, "
            + CLIENT_COLUMNS[CLIENT_TEL_PORTABLE] + " TEXT, "
            + CLIENT_COLUMNS[CLIENT_LATITUDE] + " REAL, "
            + CLIENT_COLUMNS[CLIENT_LONGITUDE] + " REAL, "
            + " FOREIGN KEY ("+ CLIENT_COLUMNS[CLIENT_CATEGORIE] +", "+CLIENT_COLUMNS[CLIENT_SOUS_CATEGORIE]+")"
            + " REFERENCES "+ TABLE_SOUS_CATEGORIE +"("+SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_CATEGORIE]+", "+SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_NOM]+")"
            +");";

    private static final String CREATION_TABLE_GEOLOC =
        "CREATE TABLE " + TABLE_GEOLOC + " ( "
            + GEOLOC_COLUMNS[GEOLOC_DATETIME] + " TEXT, "
            + GEOLOC_COLUMNS[GEOLOC_UTILISATEUR] + " TEXT, "
            + GEOLOC_COLUMNS[GEOLOC_LATITUDE] + " REAL, "
            + GEOLOC_COLUMNS[GEOLOC_LONGITUDE] + " REAL, "
            + " FOREIGN KEY ("+ GEOLOC_COLUMNS[GEOLOC_UTILISATEUR] +") REFERENCES "+ TABLE_UTILISATEUR +"("+UTILISATEUR_COLUMNS[UTILISATEUR_PSEUDO]+"),"
            + " PRIMARY KEY ("+ GEOLOC_COLUMNS[GEOLOC_DATETIME] +", "+GEOLOC_COLUMNS[GEOLOC_UTILISATEUR]+")"
            +");";

    private static final String CREATION_TABLE_GEOLOC_TO_ADD =
            "CREATE TABLE " + TABLE_GEOLOC_TO_ADD + " ( "
                    + GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_DATETIME] + " TEXT, "
                    + GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_UTILISATEUR] + " TEXT, "
                    + GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_LATITUDE] + " REAL, "
                    + GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_LONGITUDE] + " REAL, "
                    + " PRIMARY KEY ("+ GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_DATETIME] +", "+GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_UTILISATEUR]+")"
                    +");";

    private static final String SUPPRIMER_TABLE =
            "DROP TABLE IF EXISTS %s ;";
    
    private static DBHelper instance;
    
    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null) {
             instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Activer les contraintes clé étrangère
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE_DOSSIER);
        db.execSQL(CREATION_TABLE_DROIT);
        db.execSQL(CREATION_TABLE_UTILISATEUR);
        db.execSQL(CREATION_TABLE_CATEGORIE);
        db.execSQL(CREATION_TABLE_SOUS_CATEGORIE);
        db.execSQL(CREATION_TABLE_CLIENT);
        db.execSQL(CREATION_TABLE_GEOLOC);
        db.execSQL(CREATION_TABLE_GEOLOC_TO_ADD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int ancienneVersion, int nouvelleVersion) {
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_GEOLOC_TO_ADD));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_GEOLOC));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_CLIENT));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_SOUS_CATEGORIE));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_CATEGORIE));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_UTILISATEUR));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_DROIT));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_DOSSIER));
        onCreate(db);
    }

    private static String getSelectFrom(String table, String[] columns) {
        int taille = columns.length;
        if (taille <= 0) {
            return null;
        }
        String requete = "select ";
        for (int i=0 ; i < taille ; i++) {
            requete+= columns[i]+(i != taille-1 ? ", " : "");
        }
        requete += " from "+ table;

        return requete;
    }

    private static String getSelectFrom(String[] tables, String[]... columns) {
        int nbTable = columns.length;
        int taille;
        if (nbTable <= 0) {
            return null;
        }
        String requete = "select ";
        for (int i=0 ; i < nbTable ; i++) {
            taille = columns[i].length;
            for (int j=0 ; j < taille ; j++) {
                requete += columns[i][j]+(i == nbTable-1 && j == taille-1 ? "" : ", ");
            }
        }
        requete += " from ";
        for (int i=0 ; i < nbTable ; i++) {
            requete += tables[i]+(i == nbTable-1 ? "" : ", ");
        }

        return requete;
    }

    public static String getSelectFromDossier() {
        return getSelectFrom(TABLE_DOSSIER, DOSSIER_COLUMNS);
    }

    public static String getSelectFromDroit() {
        return getSelectFrom(TABLE_DROIT, DROIT_COLUMNS);
    }

    public static String getSelectFromUtilisateur() {
        String[][] columns = new String[][] {
            UTILISATEUR_COLUMNS, DOSSIER_COLUMNS, DROIT_COLUMNS
        };
        int nbTable = columns.length;
        int taille;
        String requete = "select ";
        for (int i=0 ; i < nbTable ; i++) {
            taille = columns[i].length;
            for (int j=0 ; j < taille ; j++) {
                requete += columns[i][j]+(i == nbTable-1 && j == taille-1 ? "" : ", ");
            }
        }
        requete += " from " + TABLE_UTILISATEUR;
        requete += " left outer join " + TABLE_DOSSIER
            + " on " + UTILISATEUR_COLUMNS[UTILISATEUR_ID_DOSSIER]
            + " = " + DOSSIER_COLUMNS[DOSSIER_ID];
        requete += " join " + TABLE_DROIT
            + " on " + UTILISATEUR_COLUMNS[UTILISATEUR_DROIT]
            + " = " + DROIT_COLUMNS[DROIT_DROIT];

        return requete;
    }

    public static String getSelectFromCategorie() {
        return getSelectFrom(TABLE_CATEGORIE, CATEGORIE_COLUMNS);
    }

    public static String getSelectFromSousCategorie() {
        String requete = getSelectFrom(new String[]{TABLE_SOUS_CATEGORIE, TABLE_CATEGORIE},
            SOUS_CATEGORIE_COLUMNS, CATEGORIE_COLUMNS);
        requete += " where (" + SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_CATEGORIE]
            + " = " + CATEGORIE_COLUMNS[CATEGORIE_NOM]+")";

        return requete;
    }

    public static String getSelectFromClient() {
        String requete = getSelectFrom(new String[]{TABLE_CLIENT, TABLE_SOUS_CATEGORIE},
            CLIENT_COLUMNS, SOUS_CATEGORIE_COLUMNS);
        requete += " where (" + CLIENT_COLUMNS[CLIENT_SOUS_CATEGORIE]
            + " = " + SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_NOM];
        requete += " and " + CLIENT_COLUMNS[CLIENT_CATEGORIE]
            + " = " + SOUS_CATEGORIE_COLUMNS[SOUS_CATEGORIE_CATEGORIE]+")";

        return requete;
    }

    public static String getSelectFromGeoloc() {
        String requete = getSelectFrom(new String[]{TABLE_GEOLOC, TABLE_UTILISATEUR, TABLE_DOSSIER, TABLE_DROIT},
                GEOLOC_COLUMNS, UTILISATEUR_COLUMNS, DOSSIER_COLUMNS, DROIT_COLUMNS);
        requete += " where (" + GEOLOC_COLUMNS[GEOLOC_UTILISATEUR]
                + " = " + UTILISATEUR_COLUMNS[UTILISATEUR_PSEUDO];
        requete += " and " + UTILISATEUR_COLUMNS[UTILISATEUR_ID_DOSSIER] + " = " + DOSSIER_COLUMNS[DOSSIER_ID];
        requete += " and " + UTILISATEUR_COLUMNS[UTILISATEUR_DROIT] + " = " + DROIT_COLUMNS[DROIT_DROIT]+")";

        return requete;
    }

    public static String getSelectFromGeolocToAdd() {
        String requete = getSelectFrom(new String[]{TABLE_GEOLOC_TO_ADD, TABLE_UTILISATEUR, TABLE_DOSSIER, TABLE_DROIT},
                GEOLOC_TO_ADD_COLUMNS, UTILISATEUR_COLUMNS, DOSSIER_COLUMNS, DROIT_COLUMNS);
        requete += " where (" + GEOLOC_TO_ADD_COLUMNS[GEOLOC_TO_ADD_UTILISATEUR]
                + " = " + UTILISATEUR_COLUMNS[UTILISATEUR_PSEUDO];
        requete += " and " + UTILISATEUR_COLUMNS[UTILISATEUR_ID_DOSSIER] + " = " + DOSSIER_COLUMNS[DOSSIER_ID];
        requete += " and " + UTILISATEUR_COLUMNS[UTILISATEUR_DROIT] + " = " + DROIT_COLUMNS[DROIT_DROIT]+")";

        return requete;
    }

    public static void main(String... args) {
        String requete = getSelectFromGeoloc();
        requete = requete.replaceAll(" (from|where)", "\n$1");
        System.out.print(requete);
    }
}
