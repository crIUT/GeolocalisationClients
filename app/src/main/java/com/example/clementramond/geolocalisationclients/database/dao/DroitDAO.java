package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Droit;

import java.util.ArrayList;

public class DroitDAO extends GeolocClientsDAO {

    private static final String WHERE_ID_EQUALS = DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT] + " ='?'";
    
    public DroitDAO(Context context) {
        super(context);
    }

    public long save(Droit droit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT], droit.getDroit());

        return database.insert(DBHelper.TABLE_DROIT, null, values);
    }

    public long update(Droit droit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT], droit.getDroit());

        long result = database.update(DBHelper.TABLE_DROIT, values, WHERE_ID_EQUALS,
            new String[] { String.valueOf(droit.getDroit()) });
        Log.d("Update Result:", "="+result);
        return result;
    }

    public int delete(Droit droit) {
        return database.delete(DBHelper.TABLE_DROIT, WHERE_ID_EQUALS,
            new String[] { String.valueOf(droit.getDroit()) });
    }

    public ArrayList<Droit> getDroits() {
        ArrayList<Droit> droits = new ArrayList<>();
        Cursor cursor = database.query(
            DBHelper.TABLE_DROIT,
            new String[] {
                DBHelper.DROIT_COLUMNS[DBHelper.DROIT_DROIT]
            },
            null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            Droit droit = new Droit(cursor.getString(0));
            droits.add(droit);
        }
        return droits;
    }

}
