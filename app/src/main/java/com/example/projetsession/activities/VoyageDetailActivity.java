package com.example.projetsession.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.dao.ReservationDAO;
import com.example.projetsession.dao.VoyageDAO;
import com.example.projetsession.modeles.Reservation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VoyageDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView voyageImage;
    private TextView voyageTitle;
    private TextView destinationText;
    private TextView dureeText;
    private TextView voyageDescription;
    private RecyclerView voyageActivitiesRecyclerView;
    private TextView voyagePrice;
    private ImageButton buttonMinus;
    private ImageButton buttonPlus;
    private TextView quantityText;
    private TextView placesRestantes;
    private Spinner dateSpinner;
    private Button bookButton;

    private int quantity = 1;
    private double basePrice = 0.0;
    private long voyageId;
    private int[] placesArray; // Available places for each date (order matching the dates array)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_detail);

        backButton = findViewById(R.id.backButton);
        voyageImage = findViewById(R.id.voyageImage);
        voyageTitle = findViewById(R.id.voyageTitle);
        destinationText = findViewById(R.id.destination);
        dureeText = findViewById(R.id.duree);
        voyageDescription = findViewById(R.id.voyageDescription);
        voyageActivitiesRecyclerView = findViewById(R.id.voyageActivitiesRecyclerView);
        voyagePrice = findViewById(R.id.voyagePrice);
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonPlus = findViewById(R.id.buttonPlus);
        quantityText = findViewById(R.id.quantityText);
        placesRestantes = findViewById(R.id.placesRestantes);
        dateSpinner = findViewById(R.id.dateSpinner);
        bookButton = findViewById(R.id.bookButton);

        backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        voyageId = intent.getLongExtra("id", -1);
        String title = intent.getStringExtra("title");
        String dest = intent.getStringExtra("destination");
        String duree = intent.getStringExtra("duree");
        String description = intent.getStringExtra("description");
        basePrice = intent.getDoubleExtra("price", 0.0);
        String[] dates = intent.getStringArrayExtra("dates");
        placesArray = intent.getIntArrayExtra("placesArray");

        if (dates == null || dates.length == 0) {
            dates = new String[]{ "2025-06-01" };
        }
        if (placesArray == null || placesArray.length == 0) {
            placesArray = new int[]{ 0 };
        }

        voyageTitle.setText(title != null ? title : "Détail du voyage");
        destinationText.setText(dest != null ? dest : "");
        dureeText.setText(duree != null ? duree : "");
        voyageDescription.setText(description != null ? description : "");
        updatePriceTotal();

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dates);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        placesRestantes.setText(placesArray[0] + " places restantes");
        dateSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                placesRestantes.setText(placesArray[position] + " places restantes");
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        voyageActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        quantityText.setText(String.valueOf(quantity));
        buttonMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                updatePriceTotal();
            }
        });
        buttonPlus.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            updatePriceTotal();
        });

        bookButton.setOnClickListener(v -> {
            final int selectedPosition = dateSpinner.getSelectedItemPosition();
            final int available = placesArray[selectedPosition];
            if (quantity > available) {
                Toast.makeText(VoyageDetailActivity.this,
                        "Impossible de réserver : nombre de places demandé supérieur aux places disponibles",
                        Toast.LENGTH_LONG).show();
                return;
            }
            final int newAvailable = available - quantity;
            placesArray[selectedPosition] = newAvailable;
            placesRestantes.setText(newAvailable + " places restantes");

            SharedPreferences sp = getSharedPreferences("SessionUtilisateur", MODE_PRIVATE);
            final String userId = sp.getString("idUtilisateur", "");

            final String selectedDate = dateSpinner.getSelectedItem().toString();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date tempDate;
            try {
                tempDate = sdf.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                tempDate = new Date();
            }
            final Date voyageDate = tempDate;
            final double finalTotalPrice = basePrice * quantity;

            // Update the available places on the server using VoyageDAO
            VoyageDAO voyageDAO = new VoyageDAO(VoyageDetailActivity.this);
            voyageDAO.mettreAJourPlacesDisponibles(voyageId, selectedDate, newAvailable, new VoyageDAO.UpdateCallback() {
                @Override
                public void onSuccess() {
                    Reservation reservation = new Reservation();
                    reservation.setVoyageId(voyageId);
                    reservation.setUtilisateurId(userId);
                    reservation.setDestination(destinationText.getText().toString());
                    reservation.setDateVoyage(voyageDate);
                    reservation.setNombrePlaces(quantity);
                    reservation.setMontantTotal(finalTotalPrice);
                    reservation.setStatut("confirmée");
                    reservation.setDateReservation(new Date());

                    ReservationDAO reservationDAO = new ReservationDAO(VoyageDetailActivity.this);
                    reservationDAO.open();
                    long newResId = reservationDAO.ajouterReservation(reservation);
                    reservationDAO.close();

                    Toast.makeText(VoyageDetailActivity.this,
                            "Réservation enregistrée. ID: " + newResId,
                            Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError(String message) {
                    Toast.makeText(VoyageDetailActivity.this,
                            "La mise à jour des places a échoué: " + message,
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void updatePriceTotal() {
        double totalPrice = basePrice * quantity;
        voyagePrice.setText(String.format("%.2f $", totalPrice));
    }
}
