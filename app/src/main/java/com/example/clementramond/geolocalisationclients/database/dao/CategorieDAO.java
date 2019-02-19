package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Categorie;

import java.util.ArrayList;

public class CategorieDAO extends GeolocClientsDBDAO {

    private static final String WHERE_ID_EQUALS = DBHelper.CATEGORIE_COLUMNS[DBHelper.CATEGORIE_NOM] + " = ?";

    public CategorieDAO(Context context) {
        super(context);
    }

    public long save(Categorie categorie) {
        return database.insert(DBHelper.TABLE_CATEGORIE, null,
            toContentValues(categorie));
    }

    public long update(Categorie categorie) {
        long result = database.update(DBHelper.TABLE_CATEGORIE,
            toContentValues(categorie), WHERE_ID_EQUALS,
            new String[] { String.valueOf(categorie.getNom()) });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(Categorie categorie) {
        return database.delete(DBHelper.TABLE_CATEGORIE, WHERE_ID_EQUALS,
            new String[] { String.valueOf(categorie.getNom()) });
    }

    private Cursor getCursorAll() {
        String requete = DBHelper.getSelectFromCategorie()
            + " order by " + DBHelper.CATEGORIE_COLUMNS[DBHelper.CATEGORIE_NOM];

        return database.rawQuery(requete, null);
    }

    public ArrayList<Categorie> getAll() {
        return cursorToListe(getCursorAll());
    }

    private Categorie fromCursor(Cursor c) {
        ArrayList<Categorie> categories = cursorToListe(c);
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return categories.get(0);
    }

    private ArrayList<Categorie> cursorToListe(Cursor c) {
        ArrayList<Categorie> categories = new ArrayList<>();
        Categorie categorie;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                categorie = new Categorie(c.getString(DBHelper.CATEGORIE_NOM));
                categories.add(categorie);
            }
        }
        c.close();

        return categories;
    }

    public static ContentValues toContentValues(Categorie categorie) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CATEGORIE_COLUMNS[DBHelper.CATEGORIE_NOM], categorie.getNom());

        return values;
    }

}
