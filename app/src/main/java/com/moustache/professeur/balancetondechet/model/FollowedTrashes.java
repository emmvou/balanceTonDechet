package com.moustache.professeur.balancetondechet.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowedTrashes {
    private static List<Trash> trashes = new ArrayList<Trash>();
    private static FollowedTrashes instance = null;

    public static FollowedTrashes getInstance() {
        if (instance == null) {
            instance = new FollowedTrashes();
        }
        return instance;
    }

    public FollowedTrashes() {
        Log.v("FollowedTrashes","Construction de la liste de déchet...");
    }

    public List<Trash> getTrashes() {
        return new ArrayList<>(trashes);
    }

    public boolean remove(Trash trash) {
        if (trashes.contains(trash)) {
            trashes.remove(trash);
            return true;
        }
        return false;
    }

    public boolean replace(List<Trash> lst){
        if(lst == null)
            return false;
        trashes = new ArrayList<>(lst);
        return true;
    }
}
