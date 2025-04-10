package com.example.projetsession.modeles;

import java.util.Date;

public class Reservation {
    private long id;
    private long voyageId;
    private String utilisateurId;
    private String destination;    // Pratique pour affichage direct
    private Date dateVoyage;
    private int nombrePlaces;
    private double montantTotal;
    private String statut;         // "confirmée" ou "annulée"
    private Date dateReservation;

    public Reservation() {
        // Constructeur vide
    }

    public Reservation(long id, long voyageId, String utilisateurId,
                       String destination, Date dateVoyage, int nombrePlaces,
                       double montantTotal, String statut, Date dateReservation) {
        this.id = id;
        this.voyageId = voyageId;
        this.utilisateurId = utilisateurId;
        this.destination = destination;
        this.dateVoyage = dateVoyage;
        this.nombrePlaces = nombrePlaces;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.dateReservation = dateReservation;
    }

    // Getters et Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(long voyageId) {
        this.voyageId = voyageId;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateVoyage() {
        return dateVoyage;
    }

    public void setDateVoyage(Date dateVoyage) {
        this.dateVoyage = dateVoyage;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
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

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }
}
