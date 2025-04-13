package com.example.projetsession.dao;

import android.content.Context;
import com.example.projetsession.modeles.Utilisateur;

public class UtilisateurDAO {
    private DatabaseHelper dbHelper;

    public UtilisateurDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insererUtilisateur(Utilisateur utilisateur) {
        return dbHelper.ajouterUtilisateur(utilisateur);
    }

    public Utilisateur getUtilisateurParId(int id) {
        return dbHelper.getUtilisateur(id);
    }

    // Fonction manquante ajoutée
    public Utilisateur getUtilisateurParEmail(String email) {
        return dbHelper.getUtilisateurParEmail(email);
    }

    public Utilisateur verifierConnexion(String email, String mdp) {
        Utilisateur utilisateur = getUtilisateurParEmail(email);
        if (utilisateur != null && utilisateur.getMdp().equals(mdp)) {
            return utilisateur;
        }
        return null;
    }
}
