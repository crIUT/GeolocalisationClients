package com.example.clementramond.geolocalisationclients.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.clementramond.geolocalisationclients.database.DBHelper;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Droit;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

import java.util.ArrayList;

public class UtilisateurDAO extends GeolocClientsDBDAO {

    private static final String WHERE_PSEUDO_EQUALS = DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_PSEUDO] + " = ?";

    public UtilisateurDAO(Context context) {
        super(context);
    }

    public long save(Utilisateur utilisateur) {
        return database.insert(DBHelper.TABLE_UTILISATEUR, null,
            toContentValues(utilisateur));
    }

    public long update(Utilisateur utilisateur) {
        long result = database.update(DBHelper.TABLE_UTILISATEUR,
            toContentValues(utilisateur), WHERE_PSEUDO_EQUALS,
            new String[] { String.valueOf(utilisateur.getPseudo()) });
        Log.d("Update Result:", "="+result);

        return result;
    }

    public int delete(Utilisateur utilisateur) {
        return database.delete(DBHelper.TABLE_UTILISATEUR, WHERE_PSEUDO_EQUALS,
            new String[] { String.valueOf(utilisateur.getPseudo()) });
    }

    private Cursor getCursorAll() {
        String requete = DBHelper.getSelectFromUtilisateur()
            + " order by " + DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_PSEUDO];

        return database.rawQuery(requete, null);
    }

    private Cursor getCursorWherePseudo(String pseudo) {
        String requete = DBHelper.getSelectFromUtilisateur()
            // Déjà le 'where' dans la chaine
            + " AND " + WHERE_PSEUDO_EQUALS;
        return database.rawQuery(requete, new String[]{pseudo});
    }

    public ArrayList<Utilisateur> getAll() {
        return cursorToListe(getCursorAll());
    }

    private Utilisateur fromCursor(Cursor c) {
        ArrayList<Utilisateur> utilisateurs = cursorToListe(c);
        if (utilisateurs == null || utilisateurs.isEmpty()) {
            return null;
        }
        return utilisateurs.get(0);
    }
    
    private ArrayList<Utilisateur> cursorToListe(Cursor c) {
        ArrayList<Utilisateur> utilisateurs = new ArrayList<>();
        Utilisateur utilisateur;

        Dossier dossier;
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                utilisateur = new Utilisateur();
                utilisateur.setPseudo(c.getString(DBHelper.UTILISATEUR_PSEUDO));
                utilisateur.setNom(c.isNull(DBHelper.UTILISATEUR_NOM)?null:c.getString(DBHelper.UTILISATEUR_NOM));
                utilisateur.setPrenom(c.isNull(DBHelper.UTILISATEUR_PRENOM)?null:c.getString(DBHelper.UTILISATEUR_PRENOM));
                utilisateur.setMdp(c.getString(DBHelper.UTILISATEUR_MDP));

                if (c.isNull(DBHelper.UTILISATEUR_COLUMNS.length+DBHelper.DOSSIER_ID)) {
                    dossier = null;
                } else {
                    dossier = new Dossier();
                    dossier.setId(c.getInt(DBHelper.UTILISATEUR_COLUMNS.length+DBHelper.DOSSIER_ID));
                    dossier.setNom(c.getString(DBHelper.UTILISATEUR_COLUMNS.length+DBHelper.DOSSIER_NOM));
                }
                utilisateur.setDossier(dossier);

                utilisateur.setDroit(new Droit(c.getString(DBHelper.UTILISATEUR_DROIT)));
                utilisateurs.add(utilisateur);
            }
        }
        c.close();
        
        return utilisateurs;
    }

    public static ContentValues toContentValues(Utilisateur utilisateur) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_PSEUDO], utilisateur.getPseudo());
        values.put(DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_NOM], utilisateur.getNom());
        values.put(DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_PRENOM], utilisateur.getPrenom());
        values.put(DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_MDP], utilisateur.getMdp());
        Dossier dossier_utilisateur = utilisateur.getDossier();
        values.put(DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_ID_DOSSIER], (dossier_utilisateur==null?null:dossier_utilisateur.getId()));
        values.put(DBHelper.UTILISATEUR_COLUMNS[DBHelper.UTILISATEUR_DROIT], utilisateur.getDroit().getDroit());

        return values;
    }

    public Utilisateur getFromPseudo(String pseudo) {
        return fromCursor(getCursorWherePseudo(pseudo));
    }
}
