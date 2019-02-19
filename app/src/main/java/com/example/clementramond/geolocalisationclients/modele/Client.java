package com.example.clementramond.geolocalisationclients.modele;

public class Client {

    private int id;

    private SousCategorie sousCategorie;

    private String nom;

    private String prenom;

    private String codePostal;

    private String telephoneFixe;

    private String telephonePortable;

    private Double latitude;

    private Double longitude;

    public Client() {
        super();
    }

    public Client(int id, SousCategorie sousCategorie, String nom, String prenom,
                  String codePostal, String telephoneFixe, String telephonePortable,
                  Double latitude, Double longitude) {
        this.id = id;
        this.sousCategorie = sousCategorie;
        this.nom = nom;
        this.prenom = prenom;
        this.codePostal = codePostal;
        this.telephoneFixe = telephoneFixe;
        this.telephonePortable = telephonePortable;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    public String getTelephoneFixe() {
        return telephoneFixe;
    }

    public void setTelephoneFixe(String telephone_fixe) {
        this.telephoneFixe = telephone_fixe;
    }

    public String getTelephonePortable() {
        return telephonePortable;
    }

    public void setTelephonePortable(String telephone_portable) {
        this.telephonePortable = telephone_portable;
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
        return id+"/"+sousCategorie+"/"+nom+"/"+codePostal;
    }
}
