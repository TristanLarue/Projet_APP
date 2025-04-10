package com.example.projetsession.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetsession.R;
import com.example.projetsession.dao.ReservationDAO;
import com.example.projetsession.modeles.Reservation;
import com.example.projetsession.adaptateurs.ReservationAdapter;

import java.util.List;

public class HistoriqueActivity extends AppCompatActivity {

    private RecyclerView historiqueRecyclerView;
    private ReservationAdapter reservationAdapter;
    private ReservationDAO reservationDAO;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        // Set up the RecyclerView
        historiqueRecyclerView = findViewById(R.id.historiqueRecyclerView);
        historiqueRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the current userId from SharedPreferences (set during login)
        SharedPreferences sp = getSharedPreferences("SessionUtilisateur", MODE_PRIVATE);
        userId = sp.getString("idUtilisateur", "");

        // Retrieve the list of reservations for the current user from the SQLite database
        reservationDAO = new ReservationDAO(this);
        reservationDAO.open();
        List<Reservation> reservations = reservationDAO.getReservationsUtilisateur(userId);
        reservationDAO.close();

        // Set up the adapter with a listener that cancels the reservation when the cancel button is pressed.
        reservationAdapter = new ReservationAdapter(reservations, new ReservationAdapter.OnReservationClickListener() {
            @Override
            public void onReservationClick(Reservation reservation) {
                // When the cancel button is clicked, cancel the reservation in the database.
                reservationDAO.open();
                int rowsAffected = reservationDAO.annulerReservation(reservation.getId());
                reservationDAO.close();
                if (rowsAffected > 0) {
                    Toast.makeText(HistoriqueActivity.this, "Réservation annulée", Toast.LENGTH_SHORT).show();
                    // Remove the reservation from the list and notify the adapter.
                    reservations.remove(reservation);
                    reservationAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HistoriqueActivity.this, "Erreur lors de l'annulation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        historiqueRecyclerView.setAdapter(reservationAdapter);
    }
}
