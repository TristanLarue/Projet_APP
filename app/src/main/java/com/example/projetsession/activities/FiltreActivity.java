package com.example.projetsession.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projetsession.R;

public class FiltreActivity extends AppCompatActivity {
    private Spinner destinationSpinner, dateSpinner;
    private SeekBar budgetSeekBar;
    private TextView budgetValue;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtres);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        destinationSpinner = findViewById(R.id.destinationSpinner);
        dateSpinner = findViewById(R.id.dateSpinner);
        budgetSeekBar = findViewById(R.id.budgetSeekBar);
        budgetValue = findViewById(R.id.budgetValue);
        applyButton = findViewById(R.id.applyButton);
        String[] destinations = {
                "Toutes les destinations",
                "Montréal, Québec",
                "Québec, Québec",
                "Gaspésie, Québec",
                "Laurentides, Québec",
                "Banff, Alberta",
                "Vancouver, Colombie-Britannique",
                "Niagara Falls, Ontario",
                "Ottawa, Ontario"
        };
        ArrayAdapter<String> destAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinations);
        destAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destAdapter);
        String[] dates = {
                "Toutes les dates",
                "2025-03-10",
                "2025-04-10",
                "2025-05-10",
                "2025-05-30",
                "2025-06-25",
                "2025-07-10",
                "2025-08-12",
                "2025-09-20"
        };
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);
        budgetSeekBar.setProgress(2000);
        budgetValue.setText("2000$");
        budgetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                budgetValue.setText(progress + "$");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        applyButton.setOnClickListener(v -> {
            String destination = destinationSpinner.getSelectedItem().toString();
            String departureDate = dateSpinner.getSelectedItem().toString();
            double budgetMax = budgetSeekBar.getProgress();
            Intent intent = new Intent();
            intent.putExtra("destination_filter", destination);
            intent.putExtra("budget_max", budgetMax);
            intent.putExtra("departure_date", departureDate);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}
