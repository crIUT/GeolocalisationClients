package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Categorie;
import com.example.clementramond.geolocalisationclients.modele.Client;
import com.example.clementramond.geolocalisationclients.modele.SousCategorie;

import java.util.ArrayList;

public class ClientDAO extends GeolocClientsDBDAO {

    private static final String WHERE_ID_EQUALS = DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_ID] + " = ?";

    public ClientDAO(Context context) {
        super(context);
    }

    public long save(Client client) {
        return database.insert(DBHelper.TABLE_CLIENT, null,
            toContentValues(client));
    }

    public long update(Client client) {
        long result = database.update(DBHelper.TABLE_CLIENT,
            toContentValues(client), WHERE_ID_EQUALS,
            new String[] { String.valueOf(client.getId()) });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(Client client) {
        return database.delete(DBHelper.TABLE_CLIENT, WHERE_ID_EQUALS,
            new String[] { String.valueOf(client.getId()) });
    }

    private Cursor getCursorAll() {
        String requete = DBHelper.getSelectFromClient()
            + " order by " + DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_NOM];

        return database.rawQuery(requete, null);
    }

    private Cursor getCursorFromId(int id) {
        String requete = DBHelper.getSelectFromClient()
                + " and " + WHERE_ID_EQUALS;

        return database.rawQuery(requete, new String[]{String.valueOf(id)});
    }

    private Cursor getCursorFromCategorie(Categorie categorie) {
        String requete = DBHelper.getSelectFromClient()
                + " and " + DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_CATEGORIE] + " = ?"
                + " order by " + DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_NOM];

        return database.rawQuery(requete, new String[]{categorie.getNom()});
    }

    private Cursor getCursorFromSousCategorie(SousCategorie sousCategorie) {
        String requete = DBHelper.getSelectFromClient()
                + " and " + DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_CATEGORIE] + " = ?"
                + " and " + DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_SOUS_CATEGORIE] + " = ?"
                + " order by " + DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_NOM];

        return database.rawQuery(requete, new String[]{
                sousCategorie.getCategorie().getNom(),
                sousCategorie.getNom()
        });
    }

    public ArrayList<Client> getAll() {
        return cursorToListe(getCursorAll());
    }

    public Client getFromId(int id) {
        return fromCursor(getCursorFromId(id));
    }

    public ArrayList<Client> getFromCategorie(Categorie categorie) {
        return cursorToListe(getCursorFromCategorie(categorie));
    }

    public ArrayList<Client> getFromSousCategorie(SousCategorie sousCategorie) {
        return cursorToListe(getCursorFromSousCategorie(sousCategorie));
    }

    private Client fromCursor(Cursor c) {
        ArrayList<Client> clients = cursorToListe(c);
        if (clients == null || clients.isEmpty()) {
            return null;
        }
        return clients.get(0);
    }
    
    private ArrayList<Client> cursorToListe(Cursor c) {
        ArrayList<Client> clients = new ArrayList<>();
        Client client;
        SousCategorie sousCategorie;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                client = new Client();

                client.setId(c.getInt(DBHelper.CLIENT_ID));

                sousCategorie = new SousCategorie();
                sousCategorie.setCategorie(new Categorie(c.getString(DBHelper.CLIENT_CATEGORIE)));
                sousCategorie.setNom(c.getString(DBHelper.CLIENT_SOUS_CATEGORIE));
                client.setSousCategorie(sousCategorie);

                client.setNom(c.getString(DBHelper.CLIENT_NOM));
                client.setPrenom(c.isNull(DBHelper.CLIENT_PRENOM)?null:c.getString(DBHelper.CLIENT_PRENOM));
                client.setCodePostal(c.getString(DBHelper.CLIENT_CP));
                client.setTelephoneFixe(c.isNull(DBHelper.CLIENT_TEL_FIXE)?null:c.getString(DBHelper.CLIENT_TEL_FIXE));
                client.setTelephonePortable(c.isNull(DBHelper.CLIENT_TEL_PORTABLE)?null:c.getString(DBHelper.CLIENT_TEL_PORTABLE));
                client.setLatitude(c.isNull(DBHelper.CLIENT_LATITUDE)?null:c.getDouble(DBHelper.CLIENT_LATITUDE));
                client.setLongitude(c.isNull(DBHelper.CLIENT_LONGITUDE)?null:c.getDouble(DBHelper.CLIENT_LONGITUDE));

                clients.add(client);
            }
        }
        c.close();
        
        return clients;
    }

    public static ContentValues toContentValues(Client client) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_ID], client.getId());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_CATEGORIE],
            client.getSousCategorie().getCategorie().getNom());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_SOUS_CATEGORIE],
            client.getSousCategorie().getNom());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_NOM], client.getNom());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_PRENOM], client.getPrenom());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_CP], client.getCodePostal());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_TEL_FIXE], client.getTelephoneFixe());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_TEL_PORTABLE], client.getTelephonePortable());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_LATITUDE], client.getLatitude());
        values.put(DBHelper.CLIENT_COLUMNS[DBHelper.CLIENT_LONGITUDE], client.getLongitude());

        return values;
    }
}
