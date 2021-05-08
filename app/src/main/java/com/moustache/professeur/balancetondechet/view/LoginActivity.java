package com.moustache.professeur.balancetondechet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.controllers.UserController;
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
    private UserController userController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        connexionButton = (Button) findViewById(R.id.connexionButton);
        emailTextfield = (EditText) findViewById(R.id.emailTextfield);
        passwordTextfield = (EditText) findViewById(R.id.passwordTextfield);
        userController = new UserController();

        connexionButton.setOnClickListener(this);

    }

    @Override
    public void onClick (View v){
        String email = emailTextfield.getText().toString();
        String mdp = passwordTextfield.getText().toString();

        User currentUser = userController.login(email,mdp,this.getApplicationContext());

        if (currentUser!=null){
            Log.v("Login","Connexion fructueuse : "+currentUser.getNom());
            Intent intentMainMenu = new Intent(this, MainMenuActivity.class);
            intentMainMenu.putExtra("user",(Parcelable)currentUser);
            startActivity(intentMainMenu);
        }
        else {
            Toast.makeText(getApplicationContext(),"Erreur email ou mot de passe",Toast.LENGTH_SHORT).show();
        }
    }

}