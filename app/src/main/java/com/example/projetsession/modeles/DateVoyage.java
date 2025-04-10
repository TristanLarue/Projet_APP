package com.example.projetsession.modeles;

public class DateVoyage {
    private String date;
    private int placesDisponibles;

    public DateVoyage() {
        // Constructeur vide
    }

    public DateVoyage(String date, int placesDisponibles) {
        this.date = date;
        this.placesDisponibles = placesDisponibles;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }
}
