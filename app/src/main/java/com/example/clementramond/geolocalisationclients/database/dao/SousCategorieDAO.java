package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Categorie;
import com.example.clementramond.geolocalisationclients.modele.SousCategorie;

import java.util.ArrayList;

public class SousCategorieDAO extends GeolocClientsDBDAO {

    private static final String WHERE_ID_EQUALS =
        DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_NOM] + " = ?"
        + " and " + DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_CATEGORIE] + " = ?";

    public SousCategorieDAO(Context context) {
        super(context);
    }

    public long save(SousCategorie sousCategorie) {
        return database.insert(DBHelper.TABLE_SOUS_CATEGORIE, null,
            toContentValues(sousCategorie));
    }

    public long update(SousCategorie sousCategorie) {
        long result = database.update(DBHelper.TABLE_SOUS_CATEGORIE,
            toContentValues(sousCategorie), WHERE_ID_EQUALS,
            new String[] {
                String.valueOf(sousCategorie.getNom()),
                String.valueOf(sousCategorie.getCategorie().getNom())
            });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(SousCategorie sousCategorie) {
        return database.delete(DBHelper.TABLE_SOUS_CATEGORIE, WHERE_ID_EQUALS,
            new String[] {
                String.valueOf(sousCategorie.getNom()),
                String.valueOf(sousCategorie.getCategorie().getNom())
            });
    }

    private Cursor getCursorAll() {
        String requete = DBHelper.getSelectFromSousCategorie()
                + " order by " + DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_CATEGORIE];

        return database.rawQuery(requete, null);
    }

    private Cursor getCursorFromCategorie(Categorie categorie) {
        String requete = DBHelper.getSelectFromSousCategorie()
                + " and " + DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_CATEGORIE] + " = ?"
                + " order by " + DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_NOM];

        return database.rawQuery(requete, new String[]{categorie.getNom()});
    }

    public ArrayList<SousCategorie> getAll() {
        return cursorToListe(getCursorAll());
    }

    public ArrayList<SousCategorie> getFromCategorie(Categorie categorie) {
        return cursorToListe(getCursorFromCategorie(categorie));
    }

    private SousCategorie fromCursor(Cursor c) {
        ArrayList<SousCategorie> sousCategories = cursorToListe(c);
        if (sousCategories == null || sousCategories.isEmpty()) {
            return null;
        }
        return sousCategories.get(0);
    }

    private ArrayList<SousCategorie> cursorToListe(Cursor c) {
        ArrayList<SousCategorie> sousCategories = new ArrayList<>();
        SousCategorie sousCategorie;
        Categorie categorie;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                sousCategorie = new SousCategorie();
                sousCategorie.setNom(c.getString(DBHelper.SOUS_CATEGORIE_NOM));
                categorie = new Categorie(
                    c.getString(DBHelper.SOUS_CATEGORIE_COLUMNS.length+DBHelper.CATEGORIE_NOM)
                );
                sousCategorie.setCategorie(categorie);
                sousCategories.add(sousCategorie);
            }
        }
        c.close();

        return sousCategories;
    }

    public static ContentValues toContentValues(SousCategorie sousCategorie) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_NOM], sousCategorie.getNom());
        values.put(DBHelper.SOUS_CATEGORIE_COLUMNS[DBHelper.SOUS_CATEGORIE_CATEGORIE], sousCategorie.getCategorie().getNom());

        return values;
    }
}
