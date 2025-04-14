package com.example.projetsession.dao;

import android.content.Context;
import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Reservation;
import com.example.projetsession.modeles.Voyage;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationDAO {
    private DatabaseHelper dbHelper;
    private DateVoyageDAO dateVoyageDAO;

    public ReservationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        dateVoyageDAO = new DateVoyageDAO(context);
    }

    public long insererReservation(Reservation reservation) {
        return dbHelper.ajouterReservation(reservation);
    }

    public List<Reservation> getReservationsUtilisateur(int utilisateurId) {
        return dbHelper.getReservationsByUtilisateur(utilisateurId);
    }

    public boolean effectuerReservation(int utilisateurId, int voyageId, int dateVoyageId, int nbPlaces, double prixUnitaire) {
        List<DateVoyage> dateVoyages = dateVoyageDAO.getDateVoyagesPourVoyage(voyageId);
        DateVoyage selectedDate = null;
        for (DateVoyage dv : dateVoyages) {
            if (dv.getId() == dateVoyageId) {
                selectedDate = dv;
                break;
            }
        }
        if (selectedDate == null || selectedDate.getNbPlacesDisponibles() < nbPlaces) return false;
        double montantTotal = prixUnitaire * nbPlaces;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateReservation = dateFormat.format(new Date());
        Reservation reservation = new Reservation();
        reservation.setUtilisateurId(utilisateurId);
        reservation.setVoyageId(voyageId);
        reservation.setDateVoyageId(dateVoyageId);
        reservation.setNbPlaces(nbPlaces);
        reservation.setMontantTotal(montantTotal);
        reservation.setStatut("confirmée");
        reservation.setDateReservation(dateReservation);
        long result = dbHelper.ajouterReservation(reservation);
        if (result > 0) {
            int nouveauNbPlaces = selectedDate.getNbPlacesDisponibles() - nbPlaces;
            dateVoyageDAO.mettreAJourPlacesDisponibles(dateVoyageId, nouveauNbPlaces);
            return true;
        }
        return false;
    }

    public boolean annulerReservation(int reservationId, int dateVoyageId, int nbPlaces) {
        try {
            DateVoyage dateVoyage = null;
            for (DateVoyage dv : dateVoyageDAO.getDateVoyagesPourVoyage(0)) {
                if (dv.getId() == dateVoyageId) {
                    dateVoyage = dv;
                    break;
                }
            }
            if (dateVoyage != null) {
                int nouveauNbPlaces = dateVoyage.getNbPlacesDisponibles() + nbPlaces;
                dateVoyageDAO.mettreAJourPlacesDisponibles(dateVoyageId, nouveauNbPlaces);
            }
            dbHelper.updateReservationStatut(reservationId, "annulée");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
