package com.example.projetsession.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.adaptateurs.TypeVoyageAdapter;
import java.util.Arrays;
import java.util.List;

public class FiltresActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Spinner destinationSpinner;
    private Spinner dateSpinner;
    private SeekBar budgetSeekBar;
    private TextView budgetValue;
    private Button applyButton;
    private RecyclerView typeVoyageRecyclerView;

    // Pour stocker la sélection du type (si aucun type n'est sélectionné, c'est une chaîne vide)
    private String selectedType = "";

    // Adaptateur pour les types de voyage
    private TypeVoyageAdapter typeAdapter;

    // Valeurs par défaut
    private static final String DEFAULT_OPTION = "Tous";
    private static final int DEFAULT_BUDGET = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtres);

        // Gestion de la StatusBar pour Android R+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(true);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Récupération des vues depuis le layout
        backButton = findViewById(R.id.backButton);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        dateSpinner = findViewById(R.id.dateSpinner);
        budgetSeekBar = findViewById(R.id.budgetSeekBar);
        budgetValue = findViewById(R.id.budgetValue);
        applyButton = findViewById(R.id.applyButton);
        typeVoyageRecyclerView = findViewById(R.id.typeVoyageRecyclerView);

        // Action du bouton de retour
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Récupération des extras pour les options dynamiques
        Intent intent = getIntent();
        String[] dynamicDestinations = intent.getStringArrayExtra("destinationsOptions");
        if (dynamicDestinations == null || dynamicDestinations.length == 0) {
            dynamicDestinations = new String[]{DEFAULT_OPTION, "Paris, France", "Tokyo, Japon", "New York, USA", "Londres, Royaume-Uni", "Rome, Italie"};
        }
        String[] dynamicDates = intent.getStringArrayExtra("datesOptions");
        if (dynamicDates == null || dynamicDates.length == 0) {
            dynamicDates = new String[]{DEFAULT_OPTION, "2025-06-01", "2025-07-15", "2025-08-20", "2025-09-15", "2025-10-10"};
        }

        // Initialisation des Spinners avec les tableaux dynamiques
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dynamicDestinations);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destinationAdapter);

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dynamicDates);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        // Récupération des filtres précédemment sauvegardés
        String initDestination = intent.getStringExtra("destination");
        String initDate = intent.getStringExtra("dateDepart");
        int initBudget = intent.getIntExtra("budget", DEFAULT_BUDGET);
        String initType = intent.getStringExtra("type");

        if (initDestination == null || initDestination.isEmpty()) {
            initDestination = DEFAULT_OPTION;
        }
        if (initDate == null || initDate.isEmpty()) {
            initDate = DEFAULT_OPTION;
        }
        if (initType == null || initType.isEmpty()) {
            initType = DEFAULT_OPTION;
        }

        int posDestination = destinationAdapter.getPosition(initDestination);
        destinationSpinner.setSelection(posDestination != -1 ? posDestination : 0);
        int posDate = dateAdapter.getPosition(initDate);
        dateSpinner.setSelection(posDate != -1 ? posDate : 0);

        // Configuration du SeekBar pour le budget
        budgetSeekBar.setMax(DEFAULT_BUDGET);
        budgetSeekBar.setProgress(initBudget);
        budgetValue.setText(initBudget + "$");

        selectedType = initType.equals(DEFAULT_OPTION) ? "" : initType;

        // Configuration du RecyclerView pour les types de voyage
        typeVoyageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<String> typesDisponibles = Arrays.asList(DEFAULT_OPTION, "Culturel", "Aventure", "Moderne", "Gastronomique", "Famille");
        typeAdapter = new TypeVoyageAdapter(typesDisponibles, selectedType, new TypeVoyageAdapter.OnTypeClickListener() {
            @Override
            public void onTypeClick(String type) {
                if (type.equals(selectedType)) {
                    selectedType = "";
                } else {
                    selectedType = type.equals(DEFAULT_OPTION) ? "" : type;
                }
                typeAdapter.setSelectedType(selectedType);
                typeAdapter.notifyDataSetChanged();
            }
        });
        typeVoyageRecyclerView.setAdapter(typeAdapter);

        // Mise à jour du budget lors d'un changement sur le SeekBar
        budgetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                budgetValue.setText(progress + "$");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Action du bouton Appliquer : renvoyer les filtres sélectionnés
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = destinationSpinner.getSelectedItem().toString();
                if (destination.equals(DEFAULT_OPTION)) {
                    destination = "";
                }
                String dateDepart = dateSpinner.getSelectedItem().toString();
                if (dateDepart.equals(DEFAULT_OPTION)) {
                    dateDepart = "";
                }
                int budget = budgetSeekBar.getProgress();
                String type = selectedType;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("destination", destination);
                resultIntent.putExtra("dateDepart", dateDepart);
                resultIntent.putExtra("budget", budget);
                resultIntent.putExtra("type", type);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
