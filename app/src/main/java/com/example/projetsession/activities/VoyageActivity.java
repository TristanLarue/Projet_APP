package com.example.projetsession.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.projetsession.R;
import com.example.projetsession.dao.ReservationDAO;
import com.example.projetsession.dao.VoyageDAO;
import com.example.projetsession.modeles.DateVoyage;
import com.example.projetsession.modeles.Voyage;
import java.util.List;

public class VoyageActivity extends AppCompatActivity {

    private VoyageDAO voyageDAO;
    private ReservationDAO reservationDAO;
    private ImageButton backButton;
    private ImageView voyageImage;
    private TextView voyageTitle, destinationText, dureeText, descriptionText, priceText, placesRestantesText, quantityText;
    private Spinner dateSpinner;
    private Button bookButton;

    // Variables pour la réservation
    private List<DateVoyage> trips;
    private int availablePlaces = 0;
    private int currentQuantity = 1;
    private double basePrice = 0;
    private int voyageId = -1;
    private int userId = -1; // Récupéré depuis l'Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_detail);

        backButton = findViewById(R.id.backButton);
        voyageImage = findViewById(R.id.voyageImage);
        voyageTitle = findViewById(R.id.voyageTitle);
        destinationText = findViewById(R.id.destination);
        dureeText = findViewById(R.id.duree);
        descriptionText = findViewById(R.id.voyageDescription);
        priceText = findViewById(R.id.voyagePrice);
        placesRestantesText = findViewById(R.id.placesRestantes);
        dateSpinner = findViewById(R.id.dateSpinner);
        quantityText = findViewById(R.id.quantityText);
        bookButton = findViewById(R.id.bookButton);

        backButton.setOnClickListener(v -> finish());

        // Récupérer les extras : id du voyage ET id de l'utilisateur
        voyageId = getIntent().getIntExtra("voyage_id", -1);
        userId = getIntent().getIntExtra("user_id", -1);

        voyageDAO = new VoyageDAO(this);
        reservationDAO = new ReservationDAO(this);
        Voyage voyage = voyageDAO.getVoyageParId(voyageId);
        if (voyage != null) {
            voyageTitle.setText(voyage.getNomVoyage());
            descriptionText.setText(voyage.getDescription());
            basePrice = voyage.getPrix();
            priceText.setText("Prix : " + basePrice + "$");
            destinationText.setText(voyage.getDestination());
            dureeText.setText("Durée : " + voyage.getDureeJours() + " jours");

            Glide.with(this)
                    .load(voyage.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(voyageImage);

            trips = voyage.getDateVoyages();
            if (trips != null && !trips.isEmpty()){
                String[] dates = new String[trips.size()];
                for (int i = 0; i < trips.size(); i++) {
                    dates[i] = trips.get(i).getDate();
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dateSpinner.setAdapter(spinnerAdapter);
                dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DateVoyage selectedTrip = trips.get(position);
                        availablePlaces = selectedTrip.getNbPlacesDisponibles();
                        placesRestantesText.setText("Places restantes : " + availablePlaces);
                        currentQuantity = 1;
                        quantityText.setText(String.valueOf(currentQuantity));
                        updatePrice();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }
        }

        ImageButton buttonPlus = findViewById(R.id.buttonPlus);
        ImageButton buttonMinus = findViewById(R.id.buttonMinus);

        buttonPlus.setOnClickListener(v -> {
            if (currentQuantity < availablePlaces) {
                currentQuantity++;
                quantityText.setText(String.valueOf(currentQuantity));
                updatePrice();
            }
        });
        buttonMinus.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                quantityText.setText(String.valueOf(currentQuantity));
                updatePrice();
            }
        });

        bookButton.setOnClickListener(v -> {
            int selectedPosition = dateSpinner.getSelectedItemPosition();
            if (selectedPosition < 0 || trips == null || trips.size() <= selectedPosition) {
                Toast.makeText(VoyageActivity.this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
                return;
            }
            DateVoyage selectedTrip = trips.get(selectedPosition);
            boolean success = reservationDAO.effectuerReservation(userId, voyageId, selectedTrip.getId(), currentQuantity, basePrice);
            if (success) {
                Toast.makeText(VoyageActivity.this, "Réservation confirmée", Toast.LENGTH_SHORT).show();
                availablePlaces -= currentQuantity;
                placesRestantesText.setText("Places restantes : " + availablePlaces);
                // Redirection vers HistoriqueActivity en transmettant également l'user_id
                Intent intent = new Intent(VoyageActivity.this, HistoriqueActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(VoyageActivity.this, "Réservation impossible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePrice(){
        double totalPrice = basePrice * currentQuantity;
        priceText.setText("Prix : " + totalPrice + "$");
    }
}
