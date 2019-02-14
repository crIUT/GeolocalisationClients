package com.example.clementramond.geolocalisationclients.modele;

public class Client {

    private int id;

    private SousCategorie sousCategorie;

    private String nom;

    private String prenom;

    private String codePostal;

    private String telephone_fixe;

    private String telephone_portable;

    private Double latitude;

    private Double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SousCategorie getSousCategorie() {
        return sousCategorie;
    }

    public void setSousCategorie(SousCategorie sousCategorie) {
        this.sousCategorie = sousCategorie;
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

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getTelephone_fixe() {
        return telephone_fixe;
    }

    public void setTelephone_fixe(String telephone_fixe) {
        this.telephone_fixe = telephone_fixe;
    }

    public String getTelephone_portable() {
        return telephone_portable;
    }

    public void setTelephone_portable(String telephone_portable) {
        this.telephone_portable = telephone_portable;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return nom+" "+prenom;
    }
}
