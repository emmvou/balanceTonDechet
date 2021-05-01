package com.moustache.professeur.balancetondechet.persistance;

import android.content.Context;
import android.util.Log;

import com.moustache.professeur.balancetondechet.model.Trash;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadTrashes {

    public static ArrayList<Trash> chargerDechets (Context context) throws IOException, ClassNotFoundException {

        FileInputStream fis = context.openFileInput("saveFile.json");

        ObjectInputStream is = new ObjectInputStream(fis);

        ArrayList<Trash> trashes = (ArrayList<Trash>) is.readObject();
        for (Trash t: trashes){
            Log.v("LIST",t.toString());
        }

        is.close();

        fis.close();

        return trashes;

    }

}
