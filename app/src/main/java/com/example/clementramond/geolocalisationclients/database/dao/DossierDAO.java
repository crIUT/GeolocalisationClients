package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Dossier;

import java.util.ArrayList;

public class DossierDAO extends GeolocClientsDAO {

    private static final String WHERE_ID_EQUALS = DBHelper.DOSSIER_COLUMNS[DBHelper.DOSSIER_ID] + " = ?";

    public DossierDAO(Context context) {
        super(context);
    }

    public long save(Dossier dossier) {
        return database.insert(DBHelper.TABLE_DOSSIER, null,
            dossierToContentValues(dossier));
    }

    public long update(Dossier dossier) {
        long result = database.update(DBHelper.TABLE_DOSSIER,
            dossierToContentValues(dossier), WHERE_ID_EQUALS,
            new String[] { String.valueOf(dossier.getId()) });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(Dossier dossier) {
        return database.delete(DBHelper.TABLE_DOSSIER, WHERE_ID_EQUALS,
            new String[] { String.valueOf(dossier.getId()) });
    }

    private Cursor getCursorAllDossiers() {
        String requete = DBHelper.getSelectFromDossier()
            + " order by " + DBHelper.DOSSIER_COLUMNS[DBHelper.DOSSIER_NOM];

        return database.rawQuery(requete, null);
    }

    public ArrayList<Dossier> getAllDossiers() {
        return cursorToListeDossier(getCursorAllDossiers());
    }

    private Dossier cursorToDossier(Cursor c) {
        ArrayList<Dossier> dossiers = cursorToListeDossier(c);
        if (dossiers == null || dossiers.isEmpty()) {
            return null;
        }
        return dossiers.get(0);
    }
    
    private ArrayList<Dossier> cursorToListeDossier(Cursor c) {
        ArrayList<Dossier> dossiers = new ArrayList<>();
        Dossier dossier;
        
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                dossier = new Dossier();
                dossier.setId(c.getInt(DBHelper.DOSSIER_ID));
                dossier.setNom(c.getString(DBHelper.DOSSIER_NOM));
                dossiers.add(dossier);
            }
        }
        c.close();
        
        return dossiers;
    }

    public static ContentValues dossierToContentValues(Dossier dossier) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DOSSIER_COLUMNS[DBHelper.DOSSIER_ID], dossier.getId());
        values.put(DBHelper.DOSSIER_COLUMNS[DBHelper.DOSSIER_NOM], dossier.getNom());

        return values;
    }

}
