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
        trashes.add(new Trash("Micro-onde", "Micro-onde encore utilisable", new TrashPin(43.6173, 7.0724), "test@gmail.com", "", Type.PORTABLE));
        trashes.add(new Trash("Carcasse de voiture", "Carosserie de fiat 500 calcinée et rouillée", new TrashPin(43.6161, 7.0732), false, true, "test@gmail.com", "", Type.TRES_ENCOMBRANT));
        trashes.add(new Trash("Tessons de bouteille", "Tessons de plusieurs bouteilles de bière", new TrashPin(43.6173, 7.0760), false, true, "test@gmail.com", "", Type.PORTABLE));
        trashes.add(new Trash("Vieil écran HS", "Écran inutilisé depuis des lustres, mis au rebus par les laboratoires", new TrashPin(43.6167, 7.0710), false, true, "test@gmail.com", "", Type.ENCOMBRANT));
        trashes.add(new Trash("Canapé vétuste", "Vieux canapé des années 50", new TrashPin(43.6164, 7.0782), false, true, "test@gmail.com", "", Type.TRES_ENCOMBRANT));
        trashes.add(new Trash("Machine à laver", "Machine à laver rouillée et éventrée", new TrashPin(46.1383983941, 5.94551653868), false, true, "test@gmail.com", "", Type.TRES_ENCOMBRANT));
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
