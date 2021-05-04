package com.moustache.professeur.balancetondechet.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Trashes {
    private static List<Trash> trashes = new ArrayList<Trash>();
    private static Trashes instance= null;

    public static Trashes getInstance(){
        if (instance==null){
            instance= new Trashes();
        }
        return instance;
    }

    public Trashes(){
        Log.v("Trashes","Construction de la liste de déchet...");
    }

    public void add (Trash t){
        trashes.add(t);
    }

    public void remove( int index ){
        FollowedTrashes.getInstance().remove(trashes.remove(index));
    }

    public List<Trash> getTrashes() {
        return trashes;
    }
}
