package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Dossier;

import java.util.ArrayList;

public class DossierDAO extends GeolocClientsDAO {

    private static final String WHERE_ID_EQUALS = DBHelper.DOSSIER_ID + " =?";

    public DossierDAO(Context context) {
        super(context);
    }

    public long save(Dossier dossier) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DOSSIER_ID, dossier.getId());
        values.put(DBHelper.DOSSIER_NOM, dossier.getNom());

        return database.insert(DBHelper.TABLE_DOSSIER, null, values);
    }

    public long update(Dossier dossier) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DOSSIER_ID, dossier.getId());
        values.put(DBHelper.DOSSIER_NOM, dossier.getNom());

        long result = database.update(DBHelper.TABLE_DOSSIER, values, WHERE_ID_EQUALS,
            new String[] { String.valueOf(dossier.getId()) });
        Log.d("Update Result:", "="+result);
        return result;
    }

    public int delete(Dossier dossier) {
        return database.delete(DBHelper.TABLE_DOSSIER, WHERE_ID_EQUALS,
            new String[] { String.valueOf(dossier.getId()) });
    }

    public ArrayList<Dossier> getDossiers() {
        ArrayList<Dossier> dossiers = new ArrayList<>();
        Cursor cursor = database.query(
            DBHelper.TABLE_DOSSIER,
            new String[] {
                DBHelper.DOSSIER_ID,
                DBHelper.DOSSIER_NOM
            },
            null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            Dossier dossier = new Dossier();
            dossier.setId(cursor.getInt(0));
            dossier.setNom(cursor.getString(1));
            dossiers.add(dossier);
        }
        return dossiers;
    }

}
