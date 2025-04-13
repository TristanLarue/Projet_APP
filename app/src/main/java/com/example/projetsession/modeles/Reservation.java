package com.example.projetsession.modeles;

public class Reservation {
    private int id;
    private int utilisateurId;
    private int voyageId;
    private int dateVoyageId;
    private int nbPlaces;
    private double montantTotal;
    private String statut;
    private String dateReservation;

    public Reservation() {}

    public Reservation(int id, int utilisateurId, int voyageId, int dateVoyageId, int nbPlaces,
                       double montantTotal, String statut, String dateReservation) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.voyageId = voyageId;
        this.dateVoyageId = dateVoyageId;
        this.nbPlaces = nbPlaces;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.dateReservation = dateReservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

    public int getDateVoyageId() {
        return dateVoyageId;
    }

    public void setDateVoyageId(int dateVoyageId) {
        this.dateVoyageId = dateVoyageId;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }
}