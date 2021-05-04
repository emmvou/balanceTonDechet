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
        pendingTrashes.add(new Trash("TV cathodique","TV cathodique avec écran brisé en morceau au 8 rue de la Chappelle",new TrashPin(43.6173,7.0747), false, true,"test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg",Type.ENCOMBRANT));
        pendingTrashes.add(new Trash("Meuble en bois","Meuble en bois au bord de la route mirabeau",new TrashPin(43.6168,7.0743), false, true, "test@gmail.com","/storage/emulated/0/DCIM/Camera/IMG_20210503_132915.jpg",Type.ENCOMBRANT));
        pendingTrashes.add(new Trash("Micro-onde", "Micro-onde encore utilisable", new TrashPin(43.6173, 7.0724), "test@gmail.com", "", Type.PORTABLE));
        pendingTrashes.add(new Trash("Carcasse de voiture", "Carosserie de fiat 500 calcinée et rouillée", new TrashPin(43.6161, 7.0732), false, true, "test@gmail.com", "", Type.TRES_ENCOMBRANT));
        pendingTrashes.add(new Trash("Tessons de bouteille", "Tessons de plusieurs bouteilles de bière", new TrashPin(43.6173, 7.0760), false, true, "test@gmail.com", "", Type.PORTABLE));
        pendingTrashes.add(new Trash("Vieil écran HS", "Écran inutilisé depuis des lustres, mis au rebus par les laboratoires", new TrashPin(43.6167, 7.0710), false, true, "test@gmail.com", "", Type.ENCOMBRANT));
        pendingTrashes.add(new Trash("Canapé vétuste", "Vieux canapé des années 50", new TrashPin(43.6164, 7.0782), false, true, "test@gmail.com", "", Type.TRES_ENCOMBRANT));
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
