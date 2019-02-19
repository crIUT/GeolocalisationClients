package com.example.clementramond.geolocalisationclients.modele;

public class Utilisateur {

    private String pseudo;

    private String nom;

    private String prenom;

    private String mdp;

    private Dossier dossier;

    private Droit droit;

    public Utilisateur() {
        super();
    }

    public Utilisateur(String pseudo, String nom, String prenom, String mdp, Dossier dossier, Droit droit) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.mdp = mdp;
        this.dossier = dossier;
        this.droit = droit;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public Droit getDroit() {
        return droit;
    }

    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    @Override
    public String toString() {
        return pseudo + '/' + dossier + "/" + droit;
    }
}
