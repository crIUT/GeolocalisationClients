package com.example.clementramond.geolocalisationclients.modele;

public class Droit {

    private String droit;

    public Droit(String droit) {
        this.droit = droit;
    }

    public String getDroit() {
        return droit;
    }

    public void setDroit(String droit) {
        this.droit = droit;
    }

    @Override
    public String toString() {
        return droit;
    }
}
