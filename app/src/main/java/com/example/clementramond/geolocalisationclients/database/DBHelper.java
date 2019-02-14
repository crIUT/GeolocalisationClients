package com.example.clementramond.geolocalisationclients.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // http://androidopentutorials.com/android-sqlite-join-multiple-tables-example/

    private static final String DATABASE_NAME = "geolocalisationclients.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DOSSIER = "dossier";
    public static final String TABLE_DROIT = "droit";
    public static final String TABLE_UTILISATEUR = "utilisateur";
    public static final String TABLE_CATEGORIE = "dossier";
    public static final String TABLE_SOUS_CATEGORIE = "sous_categorie";
    public static final String TABLE_CLIENT = "client";
    public static final String TABLE_GEOLOC = "geoloc";

    public static final String DOSSIER_ID = "d_id";
    public static final String DOSSIER_NOM = "d_nom";

    public static final String DROIT_DROIT = "droit";

    public static final String UTILISATEUR_PSEUDO = "pseudo";
    public static final String UTILISATEUR_NOM = "u_nom";
    public static final String UTILISATEUR_PRENOM = "u_prenom";
    public static final String UTILISATEUR_MDP = "mdp";
    public static final String UTILISATEUR_ID_DOSSIER = "id_dossier";
    public static final String UTILISATEUR_DROIT = "u_droit";

    public static final String CATEGORIE_NOM = "cat_nom";

    public static final String SOUS_CATEGORIE_NOM = "sc_nom";
    public static final String SOUS_CATEGORIE_CATEGORIE = "sc_categorie";

    public static final String CLIENT_ID = "c_id";
    public static final String CLIENT_CATEGORIE = "c_categorie";
    public static final String CLIENT_SOUS_CATEGORIE = "c_sous_categorie";
    public static final String CLIENT_NOM = "c_nom";
    public static final String CLIENT_PRENOM = "c_prenom";
    public static final String CLIENT_CP = "cp";
    public static final String CLIENT_TEL_FIXE = "tel_fixe";
    public static final String CLIENT_TEL_PORTABLE = "tel_portable";
    public static final String CLIENT_LATITUDE = "c_latitude";
    public static final String CLIENT_LONGITUDE = "c_longitude";

    public static final String GEOLOC_DATETIME = "dateTime";
    public static final String GEOLOC_UTILISATEUR = "g_utilisateur";
    public static final String GEOLOC_LATITUDE = "g_latitude";
    public static final String GEOLOC_LONGITUDE = "g_longitude";

    private static final String CREATION_TABLE_DOSSIER =
        "CREATE TABLE " + TABLE_DOSSIER + " ( "
            + DOSSIER_ID + " INTEGER PRIMARY KEY, "
            + DOSSIER_NOM + " TEXT"
            +");";

    private static final String CREATION_TABLE_DROIT =
        "CREATE TABLE " + TABLE_DROIT+ " ( "
            + DROIT_DROIT + " TEXT PRIMARY KEY"
            +");";

    private static final String CREATION_TABLE_UTILISATEUR =
        "CREATE TABLE " + TABLE_UTILISATEUR + " ( "
            + UTILISATEUR_PSEUDO + " TEXT PRIMARY KEY, "
            + UTILISATEUR_NOM + " TEXT, "
            + UTILISATEUR_PRENOM + " TEXT, "
            + UTILISATEUR_MDP + " TEXT,"
            + UTILISATEUR_ID_DOSSIER + " INTEGER,"
            + UTILISATEUR_DROIT + " TEXT, "
            + " FOREIGN KEY ("+ UTILISATEUR_ID_DOSSIER +") REFERENCES "+ TABLE_DOSSIER +"("+DOSSIER_ID+"),"
            + " FOREIGN KEY ("+ UTILISATEUR_DROIT +") REFERENCES "+ TABLE_DROIT +"("+DROIT_DROIT+")"
            +");";

    private static final String CREATION_TABLE_CATEGORIE =
        "CREATE TABLE " + TABLE_CATEGORIE + " ( "
            + CATEGORIE_NOM + " TEXT PRIMARY KEY"
            +");";

    private static final String CREATION_TABLE_SOUS_CATEGORIE =
        "CREATE TABLE " + TABLE_SOUS_CATEGORIE + " ( "
            + SOUS_CATEGORIE_NOM + " TEXT, "
            + SOUS_CATEGORIE_CATEGORIE + " TEXT, "
            + " FOREIGN KEY ("+ SOUS_CATEGORIE_CATEGORIE +") REFERENCES "+ TABLE_CATEGORIE +"("+CATEGORIE_NOM+"),"
            + " PRIMARY KEY ("+ SOUS_CATEGORIE_NOM +", "+SOUS_CATEGORIE_CATEGORIE+")"
            +");";

    private static final String CREATION_TABLE_CLIENT =
        "CREATE TABLE " + TABLE_CLIENT + " ( "
            + CLIENT_ID + " INTEGER PRIMARY KEY, "
            + CLIENT_CATEGORIE + " TEXT, "
            + CLIENT_SOUS_CATEGORIE + " TEXT, "
            + CLIENT_NOM+ " TEXT, "
            + CLIENT_PRENOM+ " TEXT, "
            + CLIENT_CP+ " TEXT, "
            + CLIENT_TEL_FIXE + " TEXT, "
            + CLIENT_TEL_PORTABLE + " TEXT, "
            + CLIENT_LATITUDE + " REAL, "
            + CLIENT_LONGITUDE + " REAL, "
            + " FOREIGN KEY ("+ CLIENT_CATEGORIE +", "+CLIENT_SOUS_CATEGORIE+")"
            + " REFERENCES "+ TABLE_SOUS_CATEGORIE +"("+SOUS_CATEGORIE_CATEGORIE+", "+SOUS_CATEGORIE_NOM+")"
            +");";

    private static final String CREATION_TABLE_GEOLOC =
        "CREATE TABLE " + TABLE_GEOLOC + " ( "
            + GEOLOC_DATETIME + " TEXT, "
            + GEOLOC_UTILISATEUR + " TEXT, "
            + GEOLOC_LATITUDE + " REAL, "
            + GEOLOC_LONGITUDE + " REAL, "
            + " FOREIGN KEY ("+ GEOLOC_UTILISATEUR +") REFERENCES "+ TABLE_UTILISATEUR +"("+UTILISATEUR_PSEUDO+"),"
            + " PRIMARY KEY ("+ GEOLOC_DATETIME +", "+GEOLOC_UTILISATEUR+")"
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int ancienneVersion, int nouvelleVersion) {
        db.execSQL(String.format(SUPPRIMER_TABLE, CREATION_TABLE_GEOLOC));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_CLIENT));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_SOUS_CATEGORIE));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_CATEGORIE));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_UTILISATEUR));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_DROIT));
        db.execSQL(String.format(SUPPRIMER_TABLE, TABLE_DOSSIER));
        onCreate(db);
    }
}
