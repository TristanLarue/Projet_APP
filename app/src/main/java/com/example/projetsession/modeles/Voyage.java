package com.example.projetsession.modeles;

import java.util.ArrayList;
import java.util.List;

public class Voyage {
    private int id;
    private String nomVoyage;
    private String description;
    private double prix;
    private String destination;
    private String imageUrl;
    private int dureeJours;
    private String typeDeVoyage;
    private String activitesIncluses;
    private List<DateVoyage> dateVoyages;

    public Voyage() {
        dateVoyages = new ArrayList<>();
    }

    public Voyage(int id, String nomVoyage, String description, double prix, String destination,
                  String imageUrl, int dureeJours, String typeDeVoyage, String activitesIncluses) {
        this.id = id;
        this.nomVoyage = nomVoyage;
        this.description = description;
        this.prix = prix;
        this.destination = destination;
        this.imageUrl = imageUrl;
        this.dureeJours = dureeJours;
        this.typeDeVoyage = typeDeVoyage;
        this.activitesIncluses = activitesIncluses;
        this.dateVoyages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomVoyage() {
        return nomVoyage;
    }

    public void setNomVoyage(String nomVoyage) {
        this.nomVoyage = nomVoyage;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(int dureeJours) {
        this.dureeJours = dureeJours;
    }

    public String getTypeDeVoyage() {
        return typeDeVoyage;
    }

    public void setTypeDeVoyage(String typeDeVoyage) {
        this.typeDeVoyage = typeDeVoyage;
    }

    public String getActivitesIncluses() {
        return activitesIncluses;
    }

    public void setActivitesIncluses(String activitesIncluses) {
        this.activitesIncluses = activitesIncluses;
    }

    public List<DateVoyage> getDateVoyages() {
        return dateVoyages;
    }

    public void setDateVoyages(List<DateVoyage> dateVoyages) {
        this.dateVoyages = dateVoyages;
    }

    public void ajouterDateVoyage(DateVoyage dateVoyage) {
        this.dateVoyages.add(dateVoyage);
    }
}