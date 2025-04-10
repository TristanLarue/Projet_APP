package com.example.projetsession.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetsession.R;
import com.example.projetsession.adaptateurs.VoyageAdapter;
import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Voyage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccueilActivity extends AppCompatActivity {

    private static final int FILTER_REQUEST_CODE = 100;
    private static final String JSON_URL = "http://10.0.2.2:3000/voyages";

    private EditText searchEdit;
    private Button filtersButton, historyButton;
    private RecyclerView destinationRecyclerView;

    private RequestQueue requestQueue;
    private ArrayList<Voyage> listeVoyages;
    private ArrayList<Voyage> listeVoyagesOriginale;
    private VoyageAdapter voyageAdapter;

    // Filters from FiltresActivity
    private String filtreDestination = "";
    private String filtreDateDepart = "";
    private int filtreBudget = 0;
    private String filtreType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        searchEdit = findViewById(R.id.searchEdit);
        filtersButton = findViewById(R.id.filtersButton);
        historyButton = findViewById(R.id.historyButton);
        destinationRecyclerView = findViewById(R.id.destinationRecyclerView);
        destinationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listeVoyages = new ArrayList<>();
        listeVoyagesOriginale = new ArrayList<>();

        // Create the VoyageAdapter that launches VoyageDetailActivity when a voyage item is clicked.
        voyageAdapter = new VoyageAdapter(listeVoyages, voyage -> {
            Intent detailIntent = new Intent(AccueilActivity.this, VoyageDetailActivity.class);
            // Pass voyage data:
            detailIntent.putExtra("id", voyage.getId());
            detailIntent.putExtra("title", voyage.getDestination());
            detailIntent.putExtra("destination", voyage.getDestination());
            detailIntent.putExtra("description", voyage.getDescription());
            detailIntent.putExtra("price", voyage.getPrix());
            detailIntent.putExtra("duree", "7 jours");

            // Prepare arrays for the available travel dates and their corresponding available places:
            int size = voyage.getDates().size();
            String[] dates = new String[size];
            int[] placesArray = new int[size];
            for (int i = 0; i < size; i++) {
                DateVoyage dv = voyage.getDates().get(i);
                dates[i] = dv.getDate();
                placesArray[i] = dv.getPlacesDisponibles();
            }
            detailIntent.putExtra("dates", dates);
            detailIntent.putExtra("placesArray", placesArray);

            startActivity(detailIntent);
        });
        destinationRecyclerView.setAdapter(voyageAdapter);

        requestQueue = Volley.newRequestQueue(this);
        chargerVoyages();

        filtersButton.setOnClickListener(v -> {
            Intent filtreIntent = new Intent(AccueilActivity.this, FiltresActivity.class);
            filtreIntent.putExtra("destination", filtreDestination);
            filtreIntent.putExtra("dateDepart", filtreDateDepart);
            int budgetToSend = (filtreBudget == 0) ? 5000 : filtreBudget;
            filtreIntent.putExtra("budget", budgetToSend);
            filtreIntent.putExtra("type", filtreType);
            startActivityForResult(filtreIntent, FILTER_REQUEST_CODE);
        });

        historyButton.setOnClickListener(v ->
                startActivity(new Intent(AccueilActivity.this, HistoriqueActivity.class)));

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                appliquerTousLesFiltres(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void chargerVoyages() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listeVoyagesOriginale.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject voyageJson = response.getJSONObject(i);
                                String idStr = voyageJson.optString("id", "0");
                                long id = 0;
                                try {
                                    id = Long.parseLong(idStr);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String destination = voyageJson.optString("destination", "");
                                String description = voyageJson.optString("description", "");
                                double prix = voyageJson.optDouble("prix", 0.0);
                                String image = voyageJson.optString("image", "");
                                String type = voyageJson.optString("type", "");

                                JSONArray datesArray = voyageJson.optJSONArray("dates");
                                ArrayList<DateVoyage> listeDates = new ArrayList<>();
                                if (datesArray != null) {
                                    for (int j = 0; j < datesArray.length(); j++) {
                                        JSONObject dateObj = datesArray.getJSONObject(j);
                                        String dateStr = dateObj.optString("date", "");
                                        int places = dateObj.optInt("placesDisponibles", 0);
                                        listeDates.add(new DateVoyage(dateStr, places));
                                    }
                                }

                                Voyage voyage = new Voyage();
                                voyage.setId(id);
                                voyage.setDestination(destination);
                                voyage.setDescription(description);
                                voyage.setPrix(prix);
                                voyage.setImage(image);
                                voyage.setType(type);
                                voyage.setDates(listeDates);
                                listeVoyagesOriginale.add(voyage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        appliquerTousLesFiltres(searchEdit.getText().toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccueilActivity.this,
                                "Erreur de chargement des voyages.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    private void appliquerTousLesFiltres(String rechercheTextuelle) {
        ArrayList<Voyage> filteredList = new ArrayList<>();
        for (Voyage v : listeVoyagesOriginale) {
            boolean correspondRecherche = v.getDestination().toLowerCase().contains(rechercheTextuelle.toLowerCase());
            boolean correspondDestination = filtreDestination.isEmpty() || v.getDestination().toLowerCase().contains(filtreDestination.toLowerCase());
            boolean correspondType = filtreType.isEmpty() || v.getType().toLowerCase().contains(filtreType.toLowerCase());
            boolean correspondBudget = (filtreBudget == 0) || (v.getPrix() <= filtreBudget);
            boolean correspondDate = false;
            if (filtreDateDepart.isEmpty()) {
                correspondDate = true;
            } else if (v.getDates() != null) {
                for (DateVoyage dv : v.getDates()) {
                    if (dv.getDate().equals(filtreDateDepart)) {
                        correspondDate = true;
                        break;
                    }
                }
            }
            if (correspondRecherche && correspondDestination && correspondType && correspondBudget && correspondDate) {
                filteredList.add(v);
            }
        }
        listeVoyages.clear();
        listeVoyages.addAll(filteredList);
        voyageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            filtreDestination = data.getStringExtra("destination") != null ? data.getStringExtra("destination") : "";
            filtreDateDepart = data.getStringExtra("dateDepart") != null ? data.getStringExtra("dateDepart") : "";
            filtreBudget = data.getIntExtra("budget", 0);
            filtreType = data.getStringExtra("type") != null ? data.getStringExtra("type") : "";
            appliquerTousLesFiltres(searchEdit.getText().toString());
        }
    }
}
