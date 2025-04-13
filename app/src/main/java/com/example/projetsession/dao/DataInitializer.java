package com.example.projetsession.dao;
import android.content.Context;
import android.util.Log;
import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Utilisateur;
import com.example.projetsession.modeles.Voyage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
public class DataInitializer {
    private Context context;
    private VoyageDAO voyageDAO;
    private DateVoyageDAO dateVoyageDAO;
    private UtilisateurDAO utilisateurDAO;
    private static final String TAG = "DataInitializer";
    public DataInitializer(Context context) {
        this.context = context;
        voyageDAO = new VoyageDAO(context);
        dateVoyageDAO = new DateVoyageDAO(context);
        utilisateurDAO = new UtilisateurDAO(context);
    }
    public void initializeData() {
        String json = loadJSONFromAsset();
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray voyagesArray = jsonObject.getJSONArray("voyages");
                for (int i = 0; i < voyagesArray.length(); i++) {
                    JSONObject voyageObj = voyagesArray.getJSONObject(i);
                    Voyage voyage = new Voyage();
                    // Do not set the id; let SQLite auto-generate a unique primary key.
                    voyage.setNomVoyage(voyageObj.getString("nom_voyage"));
                    voyage.setDescription(voyageObj.getString("description"));
                    voyage.setPrix(voyageObj.getDouble("prix"));
                    voyage.setDestination(voyageObj.getString("destination"));
                    voyage.setImageUrl(voyageObj.getString("image_url"));
                    voyage.setDureeJours(voyageObj.getInt("duree_jours"));
                    voyage.setTypeDeVoyage(voyageObj.getString("type_de_voyage"));
                    voyage.setActivitesIncluses(voyageObj.getString("activites_incluses"));
                    long voyageId = voyageDAO.insererVoyage(voyage);
                    Log.d(TAG, "Voyage inserted with id: " + voyageId);
                    JSONArray tripsArray = voyageObj.getJSONArray("trips");
                    for (int j = 0; j < tripsArray.length(); j++) {
                        JSONObject tripObj = tripsArray.getJSONObject(j);
                        DateVoyage dateVoyage = new DateVoyage();
                        dateVoyage.setVoyageId((int) voyageId);
                        dateVoyage.setDate(tripObj.getString("date"));
                        dateVoyage.setNbPlacesDisponibles(tripObj.getInt("nb_places_disponibles"));
                        dateVoyageDAO.insererDateVoyage(dateVoyage);
                    }
                }
                if (jsonObject.has("clients")) {
                    JSONArray clientsArray = jsonObject.getJSONArray("clients");
                    for (int i = 0; i < clientsArray.length(); i++) {
                        JSONObject clientObj = clientsArray.getJSONObject(i);
                        String email = clientObj.getString("email").trim();
                        if (utilisateurDAO.getUtilisateurParEmail(email) == null) {
                            Utilisateur utilisateur = new Utilisateur();
                            // Let SQLite auto-generate the id for the client.
                            utilisateur.setNom(clientObj.getString("nom"));
                            utilisateur.setPrenom(clientObj.getString("prenom"));
                            utilisateur.setEmail(email);
                            utilisateur.setMdp(clientObj.getString("mdp").trim());
                            utilisateur.setAge(clientObj.getInt("age"));
                            utilisateur.setTelephone(clientObj.getString("telephone").trim());
                            utilisateur.setAdresse(clientObj.getString("adresse"));
                            long userId = utilisateurDAO.insererUtilisateur(utilisateur);
                            Log.d(TAG, "Client inserted with id: " + userId);
                        } else {
                            Log.d(TAG, "Client with email " + email + " already exists. Skipping insertion.");
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON parsing error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "voyages.json not loaded!");
        }
    }
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("voyages.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e(TAG, "Error loading JSON: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
