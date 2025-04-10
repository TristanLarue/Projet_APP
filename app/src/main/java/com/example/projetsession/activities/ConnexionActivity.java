package com.example.projetsession.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetsession.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnexionActivity extends AppCompatActivity {

    // URL vers le serveur JSON local pour les utilisateurs
    // Assurez-vous que json-server tourne sur le même port.
    private static final String JSON_URL = "http://10.0.2.2:3000/utilisateurs";

    private EditText emailEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private TextView signUpText;

    private RequestQueue requestQueue; // Volley

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Initialisation des vues
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText);

        // Initialisation de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Bouton "Se connecter"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tenterConnexion();
            }
        });

        // Lien "Pas de compte? S'inscrire"
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lance l'InscriptionActivity
                startActivity(new Intent(ConnexionActivity.this, com.example.projetsession.activities.InscriptionActivity.class));
            }
        });
    }

    /**
     * Vérifie que les champs ne sont pas vides, puis lance la méthode de validation.
     */
    private void tenterConnexion() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Veuillez entrer votre courriel.");
            emailEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Veuillez entrer votre mot de passe.");
            passwordEdit.requestFocus();
            return;
        }

        verifierIdentifiants(email, password);
    }

    /**
     * Fait un GET sur /utilisateurs et compare email + motDePasse.
     */
    private void verifierIdentifiants(String email, String password) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean trouve = false;

                        // On parcourt la liste des utilisateurs dans le JSON
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject userJson = response.getJSONObject(i);
                                String userEmail = userJson.getString("courriel");
                                String userPassword = userJson.getString("motDePasse");
                                // Selon votre fichier JSON, adaptez les clés exactes ("courriel", "motDePasse", etc.)

                                if (email.equals(userEmail) && password.equals(userPassword)) {
                                    trouve = true;
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (trouve) {
                            // Connexion réussie → AccueilActivity
                            Toast.makeText(ConnexionActivity.this,
                                    "Connexion réussie!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConnexionActivity.this, AccueilActivity.class));
                            finish(); // Optionnel: empêcher de revenir sur l'écran de connexion
                        } else {
                            Toast.makeText(ConnexionActivity.this,
                                    "Identifiants invalides, veuillez réessayer.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConnexionActivity.this,
                                "Erreur de connexion au serveur.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(request);
    }
}
