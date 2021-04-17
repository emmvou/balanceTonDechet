package com.moustache.professeur.balancetondechet.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.moustache.professeur.balancetondechet.model.User;
import com.moustache.professeur.balancetondechet.utils.JsonParser;
import com.moustache.professeur.balancetondechet.view.MainMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserController {

    public UserController(){
    }

    public User login(String emailTextField, String mdpTextField, Context applicationContext){
        String mdpJson = null;
        String emailJson = null;
        String prenomJson = null;
        String nomJson = null;

        String parsedUser = JsonParser.getJsonFromAssets(applicationContext,"users.json");
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
        if ((emailTextField.compareTo(emailJson) == 0) && (mdpTextField.compareTo(mdpJson) == 0 )){
            User currentUser = new User(emailJson,nomJson,prenomJson);
            return currentUser;
        }
        else return null;
    }

}
