package com.example.projetsession.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.dao.ReservationDAO;
import com.example.projetsession.dao.VoyageDAO;
import com.example.projetsession.modeles.Reservation;
import com.example.projetsession.adaptateurs.ReservationAdapter;
import java.util.List;
public class HistoriqueActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private ReservationDAO reservationDAO;
    private VoyageDAO voyageDAO;
    private TextView emptyTextView;
    private ImageButton backButton;
    private int userId=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        recyclerView=findViewById(R.id.historiqueRecyclerView);
        emptyTextView=findViewById(R.id.emptyTextView);
        backButton=findViewById(R.id.backButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userId=getIntent().getIntExtra("user_id",-1);
        reservationDAO=new ReservationDAO(this);
        voyageDAO=new VoyageDAO(this);
        backButton.setOnClickListener(v->finish());
        loadReservations();
    }
    private void loadReservations(){
        List<Reservation> reservations=reservationDAO.getReservationsUtilisateur(userId);
        if(reservations==null||reservations.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
        }else{
            emptyTextView.setVisibility(View.GONE);
        }
        if(adapter==null){
            adapter=new ReservationAdapter(reservations);
            adapter.setVoyageDAO(voyageDAO);
            adapter.setOnCancelClickListener(reservation->{
                boolean cancelled=reservationDAO.annulerReservation(reservation.getId(),reservation.getDateVoyageId(),reservation.getNbPlaces());
                if(cancelled){
                    Toast.makeText(HistoriqueActivity.this,"Réservation annulée",Toast.LENGTH_SHORT).show();
                    loadReservations();
                }else{
                    Toast.makeText(HistoriqueActivity.this,"Erreur lors de l'annulation",Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        }else{
            adapter.updateList(reservations);
        }
    }
}
