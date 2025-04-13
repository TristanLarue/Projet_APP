package com.example.projetsession.dao;

import android.content.Context;
import com.example.projetsession.modeles.DateVoyage;
import java.util.List;

public class DateVoyageDAO {
    private DatabaseHelper dbHelper;

    public DateVoyageDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insererDateVoyage(DateVoyage dateVoyage) {
        return dbHelper.ajouterDateVoyage(dateVoyage);
    }

    public List<DateVoyage> getDateVoyagesPourVoyage(int voyageId) {
        return dbHelper.getDateVoyagesByVoyageId(voyageId);
    }

    public void mettreAJourPlacesDisponibles(int dateVoyageId, int nbPlaces) {
        dbHelper.updateNbPlacesDisponibles(dateVoyageId, nbPlaces);
    }
}