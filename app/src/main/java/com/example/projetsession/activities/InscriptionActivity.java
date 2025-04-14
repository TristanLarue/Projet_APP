package com.example.projetsession.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projetsession.R;
import com.example.projetsession.dao.UtilisateurDAO;
import com.example.projetsession.modeles.Utilisateur;

public class InscriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        EditText nomEdit = findViewById(R.id.nomEdit);
        EditText prenomEdit = findViewById(R.id.prenomEdit);
        EditText ageEdit = findViewById(R.id.ageEdit);
        EditText telephoneEdit = findViewById(R.id.telephoneEdit);
        EditText adresseEdit = findViewById(R.id.adresseEdit);
        EditText emailEdit = findViewById(R.id.emailEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);
        Button signupButton = findViewById(R.id.signupButton);
        Button backButton = findViewById(R.id.backButton);
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(this);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomEdit.getText().toString().trim();
                String prenom = prenomEdit.getText().toString().trim();
                int age = 0;
                try {
                    age = Integer.parseInt(ageEdit.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(InscriptionActivity.this, "Invalid age", Toast.LENGTH_SHORT).show();
                    return;
                }
                String telephone = telephoneEdit.getText().toString().trim();
                String adresse = adresseEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                String mdp = passwordEdit.getText().toString().trim();
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);
                utilisateur.setAge(age);
                utilisateur.setTelephone(telephone);
                utilisateur.setAdresse(adresse);
                utilisateur.setEmail(email);
                utilisateur.setMdp(mdp);
                long id = utilisateurDAO.insererUtilisateur(utilisateur);
                if (id > 0) {
                    Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(InscriptionActivity.this, "Erreur inscription", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                startActivity(intent);
            }
        });
    }
}
