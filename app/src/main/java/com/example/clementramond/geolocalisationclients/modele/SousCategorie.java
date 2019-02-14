package com.example.clementramond.geolocalisationclients.modele;

public class SousCategorie {

    private Categorie categorie;

    private String nom;

    public SousCategorie(Categorie categorie, String nom) {
        this.categorie = categorie;
        this.nom = nom;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
