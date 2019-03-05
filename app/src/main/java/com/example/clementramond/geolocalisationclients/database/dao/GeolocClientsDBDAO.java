package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.clementramond.geolocalisationclients.database.DBHelper;

public class GeolocClientsDBDAO {
    
    protected SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context mContext;

    public GeolocClientsDBDAO(Context context) {
        this.mContext = context;
        dbHelper = DBHelper.getHelper(mContext);
        open();
    }

    public void open() throws SQLException {
        if (dbHelper == null) {
            dbHelper = DBHelper.getHelper(mContext);
        }
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database = null;
    }

    public void deleteTablesContent() {
        database.delete(DBHelper.TABLE_GEOLOC, null, null);
        database.delete(DBHelper.TABLE_CLIENT, null, null);
        database.delete(DBHelper.TABLE_SOUS_CATEGORIE, null, null);
        database.delete(DBHelper.TABLE_CATEGORIE, null, null);
        database.delete(DBHelper.TABLE_UTILISATEUR, null, null);
        database.delete(DBHelper.TABLE_DROIT, null, null);
        database.delete(DBHelper.TABLE_DOSSIER, null, null);
    }
}
