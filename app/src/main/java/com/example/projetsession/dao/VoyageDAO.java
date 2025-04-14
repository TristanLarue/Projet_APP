package com.example.projetsession.dao;

import android.content.Context;
import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Voyage;
import java.util.ArrayList;
import java.util.List;

public class VoyageDAO {
    private DatabaseHelper dbHelper;

    public VoyageDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insererVoyage(Voyage voyage) {
        return dbHelper.ajouterVoyage(voyage);
    }

    public Voyage getVoyageParId(int id) {
        return dbHelper.getVoyage(id);
    }

    public List<Voyage> getTousLesVoyages() {
        return dbHelper.getAllVoyages();
    }

    public List<Voyage> rechercherVoyages(String destination, String type, double prixMax, String date) {
        List<Voyage> tousLesVoyages = dbHelper.getAllVoyages();
        List<Voyage> resultats = new ArrayList<>();
        for (Voyage voyage : tousLesVoyages) {
            boolean correspondDestination = destination.isEmpty() || voyage.getDestination().toLowerCase().contains(destination.toLowerCase());
            boolean correspondType = type.isEmpty() || voyage.getTypeDeVoyage().equalsIgnoreCase(type);
            boolean correspondPrix = prixMax <= 0 || voyage.getPrix() <= prixMax;
            boolean correspondDate = date.isEmpty();
            if (!date.isEmpty()) {
                for (DateVoyage dateVoyage : voyage.getDateVoyages()) {
                    if (dateVoyage.getDate().equals(date) && dateVoyage.getNbPlacesDisponibles() > 0) {
                        correspondDate = true;
                        break;
                    }
                }
            }
            if (correspondDestination && correspondType && correspondPrix && correspondDate) {
                resultats.add(voyage);
            }
        }
        return resultats;
    }
}
