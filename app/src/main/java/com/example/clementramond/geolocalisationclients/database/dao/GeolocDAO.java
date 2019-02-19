package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Geolocalisation;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

public class GeolocDAO extends GeolocClientsDBDAO {

    private static final String WHERE_ID_EQUALS =
        DBHelper.GEOLOC_COLUMNS[DBHelper.GEOLOC_DATETIME] + " = ?"
            + " and " + DBHelper.GEOLOC_COLUMNS[DBHelper.GEOLOC_UTILISATEUR] + " = ?";

    public GeolocDAO(Context context) {
        super(context);
    }

    public long save(Geolocalisation geoloc) {
        return database.insert(DBHelper.TABLE_GEOLOC, null,
            toContentValues(geoloc));
    }

    public long update(Geolocalisation geoloc) {
        long result = database.update(DBHelper.TABLE_GEOLOC,
            toContentValues(geoloc), WHERE_ID_EQUALS,
            new String[] {
                String.valueOf(geoloc.getDateTime().format(Geolocalisation.MYSQL_DTF)),
                String.valueOf(geoloc.getUtilisateur().getPseudo())
            });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(Geolocalisation geoloc) {
        return database.delete(DBHelper.TABLE_GEOLOC, WHERE_ID_EQUALS,
            new String[] {
                String.valueOf(geoloc.getDateTime().format(Geolocalisation.MYSQL_DTF)),
                String.valueOf(geoloc.getUtilisateur().getPseudo())
            });
    }

    private Cursor getCursorAll() {
        String requete = DBHelper.getSelectFromGeoloc()
            + " order by " + DBHelper.GEOLOC_COLUMNS[DBHelper.GEOLOC_DATETIME];

        return database.rawQuery(requete, null);
    }

    public ArrayList<Geolocalisation> getAll() {
        return cursorToListe(getCursorAll());
    }

    private Geolocalisation fromCursor(Cursor c) {
        ArrayList<Geolocalisation> geolocs = cursorToListe(c);
        if (geolocs == null || geolocs.isEmpty()) {
            return null;
        }
        return geolocs.get(0);
    }

    private ArrayList<Geolocalisation> cursorToListe(Cursor c) {
        ArrayList<Geolocalisation> geolocs = new ArrayList<>();
        Geolocalisation geoloc;
        Utilisateur utilisateur;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                geoloc = new Geolocalisation();
                geoloc.setDateTime(LocalDateTime.parse(c.getString(DBHelper.GEOLOC_DATETIME),
                    Geolocalisation.MYSQL_DTF));
                utilisateur = new Utilisateur();
                utilisateur.setPseudo();
                utilisateur.setNom();
                utilisateur.setPrenom();
                utilisateur.setMdp();
                utilisateur.setDossier();
                utilisateur.setDroit();
                geoloc.setUtilisateur();
                
                geolocs.add(geoloc);
            }
        }
        c.close();

        return geolocs;
    }

    public static ContentValues toContentValues(Geolocalisation geoloc) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.GEOLOC_COLUMNS[DBHelper.GEOLOC_NOM], geoloc.getNom());
        values.put(DBHelper.GEOLOC_COLUMNS[DBHelper.GEOLOC_CATEGORIE], geoloc.getCategorie().getNom());

        return values;
    }

}
