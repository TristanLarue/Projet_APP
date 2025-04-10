package com.example.projetsession.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetsession.R;
import org.json.JSONException;
import org.json.JSONObject;

public class InscriptionActivity extends AppCompatActivity {

    private static final String JSON_URL = "http://10.0.2.2:3000/utilisateurs";
    // URL de la ressource "utilisateurs" sur le JSON server

    private EditText nomEdit, prenomEdit, ageEdit, telephoneEdit, adresseEdit, emailEdit, passwordEdit;
    private Button signupButton;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Récupération des vues
        nomEdit = findViewById(R.id.nomEdit);
        prenomEdit = findViewById(R.id.prenomEdit);
        ageEdit = findViewById(R.id.ageEdit);
        telephoneEdit = findViewById(R.id.telephoneEdit);
        adresseEdit = findViewById(R.id.adresseEdit);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        signupButton = findViewById(R.id.signupButton);

        // Initialisation de la file de requêtes Volley
        requestQueue = Volley.newRequestQueue(this);

        // Gestion du clic sur "Créer mon compte"
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tenterInscription();
            }
        });
    }

    /**
     * Vérifie que tous les champs sont remplis, puis fait un POST sur le serveur JSON.
     */
    private void tenterInscription() {
        String nom = nomEdit.getText().toString().trim();
        String prenom = prenomEdit.getText().toString().trim();
        String age = ageEdit.getText().toString().trim();
        String telephone = telephoneEdit.getText().toString().trim();
        String adresse = adresseEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String motDePasse = passwordEdit.getText().toString().trim();

        // Vérifications basiques
        if (TextUtils.isEmpty(nom)) {
            nomEdit.setError("Veuillez entrer votre nom.");
            nomEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(prenom)) {
            prenomEdit.setError("Veuillez entrer votre prénom.");
            prenomEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(age)) {
            ageEdit.setError("Veuillez entrer votre âge.");
            ageEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telephone)) {
            telephoneEdit.setError("Veuillez entrer votre téléphone.");
            telephoneEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(adresse)) {
            adresseEdit.setError("Veuillez entrer votre adresse.");
            adresseEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Veuillez entrer votre courriel.");
            emailEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(motDePasse)) {
            passwordEdit.setError("Veuillez entrer votre mot de passe.");
            passwordEdit.requestFocus();
            return;
        }

        // Créer l'objet JSON à poster
        JSONObject newUser = new JSONObject();
        try {
            newUser.put("nom", nom);
            newUser.put("prenom", prenom);
            newUser.put("age", Integer.parseInt(age));
            newUser.put("telephone", telephone);
            newUser.put("adresse", adresse);
            newUser.put("courriel", email);
            newUser.put("motDePasse", motDePasse);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de création de l'utilisateur.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Requête POST sur /utilisateurs
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                JSON_URL,
                newUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(InscriptionActivity.this,
                                "Compte créé avec succès!",
                                Toast.LENGTH_SHORT).show();

                        // Rediriger vers la connexion
                        Intent intent = new Intent(InscriptionActivity.this, com.example.projetsession.activities.ConnexionActivity.class);
                        startActivity(intent);
                        finish(); // fermer l'écran d'inscription
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String msg = "Erreur d'inscription.";
                        // Optionnel: extraire plus d'info
                        NetworkResponse nr = error.networkResponse;
                        if (nr != null) {
                            msg += " Code HTTP: " + nr.statusCode;
                        }
                        Toast.makeText(InscriptionActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Ajouter la requête à la file
        requestQueue.add(request);
    }
}
