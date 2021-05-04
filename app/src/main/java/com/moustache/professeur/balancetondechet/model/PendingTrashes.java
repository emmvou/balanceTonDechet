package com.moustache.professeur.balancetondechet.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class PendingTrashes extends Observable {
    private static List<Trash> pendingTrashes = new ArrayList<Trash>();
    private static PendingTrashes instance= null;

    public static PendingTrashes getInstance(){
        if (instance==null){
            instance= new PendingTrashes();
        }
        return instance;
    }

    public PendingTrashes(){
        super();
        Log.v("PendingTrashes","Construction de la liste de posts...");
        pendingTrashes.add(new Trash("Salut","je sabote la BDD",new TrashPin(43.6179,7.0747), false, true,"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg",Type.TRES_ENCOMBRANT));
        pendingTrashes.add(new Trash("Table verre","Table en verre au bord de la route mirabeau",new TrashPin(43.6165,7.0740), false, true,"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg",Type.ENCOMBRANT));
    }

    public void add (Trash t){
        pendingTrashes.add(t);
        setChanged();
        notifyObservers(t);
    }

    public void remove( int index ){
        Trash t = pendingTrashes.get(index);
        pendingTrashes.remove(index);
        setChanged();
        notifyObservers(t);
    }

    public List<Trash> getPendingTrashes() {
        return pendingTrashes;
    }
}
