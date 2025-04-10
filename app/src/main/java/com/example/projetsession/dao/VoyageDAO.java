package com.example.projetsession.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Voyage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour accéder aux données "Voyage" sur un serveur JSON.
 */
public class VoyageDAO {
    private static final String TAG = "VoyageDAO";
    // URL de base pour votre serveur JSON
    private static final String BASE_URL = "http://10.0.2.2:3000/voyages";

    private final RequestQueue requestQueue;

    public VoyageDAO(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Récupère la liste de tous les voyages du serveur JSON.
     */
    public void getAllVoyages(final VoyagesCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Voyage> listeVoyages = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject voyageJson = response.getJSONObject(i);
                                Voyage voyage = parseVoyage(voyageJson);
                                listeVoyages.add(voyage);
                            }
                            callback.onSuccess(listeVoyages);
                        } catch (JSONException e) {
                            Log.e(TAG, "Erreur d'analyse JSON", e);
                            callback.onError("Erreur d'analyse du JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur de requête Volley", error);
                        callback.onError("Erreur de connexion au serveur");
                    }
                }
        );
        requestQueue.add(request);
    }

    /**
     * Récupère un voyage par son ID depuis le serveur JSON.
     */
    public void getVoyageById(long id, final VoyageCallback callback) {
        String url = BASE_URL + "/" + id;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Voyage voyage = parseVoyage(response);
                            callback.onSuccess(voyage);
                        } catch (JSONException e) {
                            Log.e(TAG, "Erreur d'analyse JSON", e);
                            callback.onError("Impossible de lire le JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur de requête Volley", error);
                        callback.onError("Erreur de connexion au serveur");
                    }
                }
        );
        requestQueue.add(request);
    }

    /**
     * Met à jour le nombre de places disponibles pour une date d'un voyage (optionnel).
     */
    public void mettreAJourPlacesDisponibles(long voyageId, String date, int nvPlaces, final UpdateCallback callback) {
        // 1) Récupérer le voyage concerné
        getVoyageById(voyageId, new VoyageCallback() {
            @Override
            public void onSuccess(Voyage voyage) {
                // 2) Mettre à jour la valeur localement
                for (DateVoyage dv : voyage.getDates()) {
                    if (dv.getDate().equals(date)) {
                        dv.setPlacesDisponibles(nvPlaces);
                        break;
                    }
                }
                // 3) Préparer l’objet JSON pour la mise à jour
                try {
                    JSONObject jsonVoyage = buildJsonVoyage(voyage);
                    String url = BASE_URL + "/" + voyageId;
                    // 4) Envoyer un PUT pour mettre à jour le voyage
                    JsonObjectRequest putRequest = new JsonObjectRequest(
                            Request.Method.PUT,
                            url,
                            jsonVoyage,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    callback.onSuccess();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    callback.onError("Erreur lors de la mise à jour");
                                }
                            }
                    );
                    requestQueue.add(putRequest);
                } catch (JSONException e) {
                    callback.onError("Erreur de construction du JSON");
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    /**
     * Convertit un objet JSON en objet Voyage.
     */
    private Voyage parseVoyage(JSONObject obj) throws JSONException {
        Voyage voyage = new Voyage();
        voyage.setId(obj.getLong("id"));
        voyage.setDestination(obj.getString("destination"));
        voyage.setDescription(obj.getString("description"));
        voyage.setPrix(obj.getDouble("prix"));
        voyage.setImage(obj.optString("image", ""));
        voyage.setType(obj.optString("type", ""));

        JSONArray datesArray = obj.optJSONArray("dates");
        List<DateVoyage> listeDates = new ArrayList<>();
        if (datesArray != null) {
            for (int i = 0; i < datesArray.length(); i++) {
                JSONObject dObj = datesArray.getJSONObject(i);
                DateVoyage dv = new DateVoyage();
                dv.setDate(dObj.getString("date"));
                dv.setPlacesDisponibles(dObj.getInt("placesDisponibles"));
                listeDates.add(dv);
            }
        }
        voyage.setDates(listeDates);
        return voyage;
    }

    /**
     * Construit un JSONObject à partir d'un objet Voyage (pour PUT/POST).
     */
    private JSONObject buildJsonVoyage(Voyage voyage) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", voyage.getId());
        obj.put("destination", voyage.getDestination());
        obj.put("description", voyage.getDescription());
        obj.put("prix", voyage.getPrix());
        obj.put("image", voyage.getImage());
        obj.put("type", voyage.getType());

        JSONArray datesArray = new JSONArray();
        for (DateVoyage dv : voyage.getDates()) {
            JSONObject dObj = new JSONObject();
            dObj.put("date", dv.getDate());
            dObj.put("placesDisponibles", dv.getPlacesDisponibles());
            datesArray.put(dObj);
        }
        obj.put("dates", datesArray);

        return obj;
    }

    // Interfaces de callback (asynchrones)
    public interface VoyagesCallback {
        void onSuccess(List<Voyage> voyages);
        void onError(String message);
    }

    public interface VoyageCallback {
        void onSuccess(Voyage voyage);
        void onError(String message);
    }

    public interface UpdateCallback {
        void onSuccess();
        void onError(String message);
    }
}
