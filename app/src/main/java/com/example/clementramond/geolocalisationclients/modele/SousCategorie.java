package com.example.clementramond.geolocalisationclients.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class SousCategorie implements Parcelable {

    private Categorie categorie;

    private String nom;

    public SousCategorie() {
        super();
    }

    public SousCategorie(Categorie categorie, String nom) {
        this.categorie = categorie;
        this.nom = nom;
    }

    public SousCategorie(String nom) {
        this.categorie = null;
        this.nom = nom;
    }

    protected SousCategorie(Parcel in) {
        categorie = in.readParcelable(Categorie.class.getClassLoader());
        nom = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(categorie, flags);
        dest.writeString(nom);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SousCategorie> CREATOR = new Creator<SousCategorie>() {
        @Override
        public SousCategorie createFromParcel(Parcel in) {
            return new SousCategorie(in);
        }

        @Override
        public SousCategorie[] newArray(int size) {
            return new SousCategorie[size];
        }
    };

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
