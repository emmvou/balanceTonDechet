package com.moustache.professeur.balancetondechet.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Trashes extends Observable {
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
        trashes.add(new Trash("TV cathodique","TV cathodique avec écran brisé en morceau au 8 rue de la Chappelle",new TrashPin(43.6173,7.0747),"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg"));
        trashes.add(new Trash("Meuble en bois","Meuble en bois au bord de la route mirabeau",new TrashPin(43.6168,7.0743),"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg"));
    }

    public void add (Trash t){
        trashes.add(t);
        setChanged();
        notifyObservers(t);
    }

    public void remove ( Trash t ){
        trashes.remove(t);
        setChanged();
        notifyObservers(t);
    }

    public List<Trash> getTrashes() {
        return trashes;
    }
}
