package com.moustache.professeur.balancetondechet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.User;
import com.moustache.professeur.balancetondechet.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailTextfield;
    private EditText passwordTextfield;
    private Button connexionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        connexionButton = (Button) findViewById(R.id.connexionButton);
        emailTextfield = (EditText) findViewById(R.id.emailTextfield);
        passwordTextfield = (EditText) findViewById(R.id.passwordTextfield);

        connexionButton.setOnClickListener(this);

    }

    @Override
    public void onClick (View v){
        String email = emailTextfield.getText().toString();
        String mdp = passwordTextfield.getText().toString();
        String mdpJson = null;
        String emailJson = null;
        String prenomJson = null;
        String nomJson = null;

        String parsedUser = JsonParser.getJsonFromAssets(this.getApplicationContext(),"users.json");
        Log.v("JSON : ",parsedUser);

        try {
            JSONObject jObject = new JSONObject(parsedUser);
            emailJson = jObject.getString("email");
            mdpJson = jObject.getString("mdp");
            nomJson = jObject.getString("nom");
            prenomJson = jObject.getString("prenom");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Connexion de l'utilisateur
        if ((email.compareTo(emailJson) == 0) && (mdp.compareTo(mdpJson) == 0 )){
            User currentUser = new User(emailJson,nomJson,prenomJson);
            Log.v("Login","Connexion fructueuse : "+currentUser.getNom());
            Intent intentMainMenu = new Intent(this, MainMenuActivity.class);
            intentMainMenu.putExtra("user",currentUser);
            startActivity(intentMainMenu);
        }
        else {
            Toast.makeText(getApplicationContext(),"Erreur email ou mot de passe",Toast.LENGTH_SHORT).show();
        }

    }
}