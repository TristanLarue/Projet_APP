package com.example.projetsession.modeles;

public class DateVoyage {
    private int id;
    private int voyageId;
    private String date;
    private int nbPlacesDisponibles;

    public DateVoyage() {}

    public DateVoyage(int id, int voyageId, String date, int nbPlacesDisponibles) {
        this.id = id;
        this.voyageId = voyageId;
        this.date = date;
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNbPlacesDisponibles() {
        return nbPlacesDisponibles;
    }

    public void setNbPlacesDisponibles(int nbPlacesDisponibles) {
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }
}
