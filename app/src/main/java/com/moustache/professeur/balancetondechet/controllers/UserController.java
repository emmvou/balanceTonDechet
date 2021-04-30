package com.moustache.professeur.balancetondechet.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.User;
import com.moustache.professeur.balancetondechet.utils.JsonParser;
import com.moustache.professeur.balancetondechet.view.MainMenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserController {

    public UserController(){
    }

    public User login(String emailTextField, String mdpTextField, Context applicationContext){
        String mdpJson = null;
        String emailJson = null;
        String prenomJson = null;
        String nomJson = null;

        try {
            JSONObject obj = new JSONObject(JsonParser.getJsonFromAssets(applicationContext,"users.json"));
            JSONArray m_jArry = obj.getJSONArray("users");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("email"));
                String email_value = jo_inside.getString("email");
                String mdp_value = jo_inside.getString("mdp");
                if ((emailTextField.compareTo(email_value) == 0) && (mdpTextField.compareTo(mdp_value) == 0 )){
                    String nom_value = jo_inside.getString("nom");
                    String prenom_value = jo_inside.getString("prenom");
                    User currentUser = new User(email_value,nom_value,prenom_value);
                    return currentUser;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void pickUpTrash(User u, Trash t){
        u.addTrash(t);
    }

}
