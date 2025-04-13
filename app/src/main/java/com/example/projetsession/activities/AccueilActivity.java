package com.example.projetsession.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.dao.DataInitializer;
import com.example.projetsession.dao.VoyageDAO;
import com.example.projetsession.modeles.Voyage;
import com.example.projetsession.adaptateurs.VoyageAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccueilActivity extends AppCompatActivity {
    private VoyageDAO voyageDAO;
    private VoyageAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchEdit;
    private String destinationFilter = "";
    private String departureDate = "";
    private double budgetMax = 0;
    private static final int REQUEST_FILTERS = 100;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        // Récupérer l'identifiant de l'utilisateur depuis ConnexionActivity
        userId = getIntent().getIntExtra("user_id", -1);
        // Initialiser les données (si ce n'est pas déjà fait)
        new DataInitializer(this).initializeData();
        searchEdit = findViewById(R.id.searchEdit);
        recyclerView = findViewById(R.id.destinationRecyclerView);
        Button filtersButton = findViewById(R.id.filtersButton);
        Button historyButton = findViewById(R.id.historyButton);

        voyageDAO = new VoyageDAO(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadVoyages();

        searchEdit.addTextChangedListener(new TextWatcher(){
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override public void afterTextChanged(Editable s){ loadVoyages(); }
        });
        filtersButton.setOnClickListener(v ->
                startActivityForResult(new Intent(AccueilActivity.this, FiltreActivity.class), REQUEST_FILTERS)
        );
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, HistoriqueActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });

    }

    private void loadVoyages(){
        String destinationSearch = searchEdit.getText().toString().trim();
        List<Voyage> voyages = voyageDAO.rechercherVoyages(
                !destinationFilter.isEmpty() ? destinationFilter : destinationSearch,
                "",
                budgetMax,
                departureDate
        );
        // Groupement par nom de voyage pour n'avoir qu'une seule case par voyage
        Map<String, Voyage> groupedByName = new HashMap<>();
        for (Voyage v : voyages) {
            String name = v.getNomVoyage();
            if (!groupedByName.containsKey(name)) {
                groupedByName.put(name, v);
            }
        }
        List<Voyage> uniqueVoyages = new ArrayList<>(groupedByName.values());
        if(adapter == null){
            adapter = new VoyageAdapter(uniqueVoyages);
            adapter.setOnItemClickListener(voyage -> {
                Intent intent = new Intent(AccueilActivity.this, VoyageActivity.class);
                intent.putExtra("voyage_id", voyage.getId());
                intent.putExtra("user_id", userId); // si applicable
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(uniqueVoyages);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_FILTERS && resultCode == Activity.RESULT_OK && data != null){
            destinationFilter = data.getStringExtra("destination_filter");
            budgetMax = data.getDoubleExtra("budget_max", 0);
            departureDate = data.getStringExtra("departure_date");
            loadVoyages();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
