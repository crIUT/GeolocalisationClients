package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Droit;

import java.util.ArrayList;

public class DroitDAO extends GeolocClientsDBDAO {

    private static final String WHERE_DROIT_EQUALS = DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT] + " = ?";

    public DroitDAO(Context context) {
        super(context);
    }

    public long save(Droit droit) {
        return database.insert(DBHelper.TABLE_DROIT, null,
            toContentValues(droit));
    }

    public long update(Droit droit) {
        long result = database.update(DBHelper.TABLE_DROIT,
            toContentValues(droit), WHERE_DROIT_EQUALS,
            new String[] { String.valueOf(droit.getDroit()) });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(Droit droit) {
        return database.delete(DBHelper.TABLE_DROIT, WHERE_DROIT_EQUALS,
            new String[] { String.valueOf(droit.getDroit()) });
    }

    private Cursor getCursorAll() {
        String requete = DBHelper.getSelectFromDroit()
            + " order by " + DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT];

        return database.rawQuery(requete, null);
    }

    public ArrayList<Droit> getAll() {
        return cursorToListe(getCursorAll());
    }

    private Droit fromCursor(Cursor c) {
        ArrayList<Droit> droits = cursorToListe(c);
        if (droits == null || droits.isEmpty()) {
            return null;
        }
        return droits.get(0);
    }

    private ArrayList<Droit> cursorToListe(Cursor c) {
        ArrayList<Droit> droits = new ArrayList<>();
        Droit droit;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                droit = new Droit(c.getString(DBHelper.DROIT_DROIT));
                droits.add(droit);
            }
        }
        c.close();

        return droits;
    }

    public static ContentValues toContentValues(Droit droit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT], droit.getDroit());

        return values;
    }

}
