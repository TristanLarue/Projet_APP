package com.example.projetsession.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projetsession.R;
import com.example.projetsession.dao.UtilisateurDAO;
import com.example.projetsession.modeles.Utilisateur;

public class ConnexionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        EditText emailEdit = findViewById(R.id.emailEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUpText = findViewById(R.id.signUpText);
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(this);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = emailEdit.getText().toString().trim();
                String mdp = passwordEdit.getText().toString().trim();
                Utilisateur u = utilisateurDAO.verifierConnexion(email, mdp);
                if(u != null){
                    Toast.makeText(ConnexionActivity.this, "Connexion bonne", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConnexionActivity.this, AccueilActivity.class);
                    intent.putExtra("user_id", u.getId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ConnexionActivity.this, "Identifiants mauvais", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
            }
        });
    }
}
