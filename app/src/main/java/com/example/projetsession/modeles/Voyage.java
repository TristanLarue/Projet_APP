package com.example.projetsession.modeles;

import java.util.List;

public class Voyage {
    private long id;
    private String destination;
    private String description;
    private double prix;
    private String image;    // Optionnel (URL ou chemin)
    private String type;     // Aventure, culturel, etc.
    private List<DateVoyage> dates; // Liste des dates disponibles

    public Voyage() {
        // Constructeur vide
    }

    public Voyage(long id, String destination, String description,
                  double prix, String image, String type, List<DateVoyage> dates) {
        this.id = id;
        this.destination = destination;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.type = type;
        this.dates = dates;
    }

    // Getters et Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DateVoyage> getDates() {
        return dates;
    }

    public void setDates(List<DateVoyage> dates) {
        this.dates = dates;
    }
}
