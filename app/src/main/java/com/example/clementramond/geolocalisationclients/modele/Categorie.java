package com.example.clementramond.geolocalisationclients.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Categorie implements Parcelable {

    private String nom;

    public Categorie(String nom) {
        this.nom = nom;
    }

    protected Categorie(Parcel in) {
        nom = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Categorie> CREATOR = new Creator<Categorie>() {
        @Override
        public Categorie createFromParcel(Parcel in) {
            return new Categorie(in);
        }

        @Override
        public Categorie[] newArray(int size) {
            return new Categorie[size];
        }
    };

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
