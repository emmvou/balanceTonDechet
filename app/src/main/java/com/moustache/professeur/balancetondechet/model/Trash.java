package com.moustache.professeur.balancetondechet.model;

import android.content.Context;

import com.moustache.professeur.balancetondechet.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Trash {
    private TrashPin trashPin;
    private String name;
    private String desc;
    private String userEmail;
    private boolean isPickedUp = false;
    private boolean isApproved = false;

    private static final String JSON_FILENAME = "trashes.json";

    public Trash (String name, String desc, TrashPin trashPin){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
    }

    private Trash (String name, String desc, TrashPin trashPin, boolean isPickedUp, boolean isApproved, String userEmail){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
        this.isApproved = isApproved;
        this.isPickedUp = isPickedUp;
        this.userEmail = userEmail;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public TrashPin getTrashPin() {
        return trashPin;
    }

    public boolean getApprobationStatus() {
        return isApproved;
    }

    public static List<Trash> parseMultipleFromJson(Context ctxt) {
        String parsedPins = JsonParser.getJsonFromAssets(ctxt, JSON_FILENAME);

        try {
            JSONArray rawTrashes = new JSONArray(parsedPins);
            ArrayList<Trash> trashes = new ArrayList<>(rawTrashes.length());

            for (int i = 0; i < rawTrashes.length(); i++) {
                JSONObject trashElement = rawTrashes.getJSONObject(i);

                Trash t = parseFromJson(trashElement);

                trashes.add(t);
            }

            return trashes;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse json");
        }
    }

    private static Trash parseFromJson(JSONObject obj) throws JSONException {
        TrashPin pin = new TrashPin(obj);
        String name = obj.getString("name");
        String desc = obj.getString("desc");
        String userEmail = obj.getString("userEmail");
        boolean isPickedUp = obj.getBoolean("isPickedUp");
        boolean isApproved = obj.getBoolean("isApproved");

        return new Trash(name, desc, pin, isPickedUp, isApproved,userEmail);
    }
}
