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
        super();
        Log.v("Trashes","Construction de la liste de déchet...");
        trashes.add(new Trash("TV cathodique","TV cathodique avec écran brisé en morceau au 8 rue de la Chappelle",new TrashPin(43.6173,7.0747),false,true,"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg",Type.ENCOMBRANT));
        trashes.add(new Trash("Meuble en bois","Meuble en bois au bord de la route mirabeau",new TrashPin(43.6168,7.0743),false,true,"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg",Type.ENCOMBRANT));
        trashes.add(new Trash("Micro-onde", "Micro-onde encore utilisable", new TrashPin(43.6173, 7.0724),false,true, "test@gmail.com", "", Type.PORTABLE));
        trashes.add(new Trash("Carcasse de voiture", "Carosserie de fiat 500 calcinée et rouillée", new TrashPin(43.6161, 7.0732),false,true, "test@gmail.com", "", Type.TRES_ENCOMBRANT));
    }

    public void add (Trash t){
        trashes.add(t);
    }

    public void remove( int index ){
        trashes.remove(index);
    }

    public List<Trash> getTrashes() {
        return trashes;
    }
}
