package com.example.clementramond.geolocalisationclients.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.modele.Dossier;

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

        Dossier[] dossiers = new Dossier[]{
            new Dossier(1, "Dossier 1"),
            new Dossier(2, "Dossier 2"),
            new Dossier(3, "Dossier 3"),
            new Dossier(4, "Dossier 4"),
            new Dossier(5, "Dossier 5")
        };
        for (Dossier dossier : dossiers) {
            db.insert(TABLE_DOSSIER, null, DossierDAO.dossierToContentValues(dossier));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int ancienneVersion, int nouvelleVersion) {
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_GEOLOC));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_CLIENT));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_SOUS_CATEGORIE));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_CATEGORIE));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_UTILISATEUR));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_DROIT));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_DOSSIER));
        onCreate(db);
    }

    public static String getSelectFrom(String[] columns, String table) {
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

    public static String getSelectFromDossier() {
        return getSelectFrom(DOSSIER_COLUMNS, TABLE_DOSSIER);
    }

    public static String getSelectFromDroit() {
        return getSelectFrom(DROIT_COLUMNS, TABLE_DROIT);
    }

    public static String getSelectFromUtilisateur() {
        return getSelectFrom(UTILISATEUR_COLUMNS, TABLE_UTILISATEUR);
    }

    public static String getSelectFromCategorie() {
        return getSelectFrom(CATEGORIE_COLUMNS, TABLE_CATEGORIE);
    }

    public static String getSelectFromSousCategorie() {
        return getSelectFrom(SOUS_CATEGORIE_COLUMNS, TABLE_SOUS_CATEGORIE);
    }

    public static String getSelectFromClient() {
        return getSelectFrom(CLIENT_COLUMNS, TABLE_CLIENT);
    }

    public static String getSelectFromGeoloc() {
        return getSelectFrom(GEOLOC_COLUMNS, TABLE_GEOLOC);
    }
}
