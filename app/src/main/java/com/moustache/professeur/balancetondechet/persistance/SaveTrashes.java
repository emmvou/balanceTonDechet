package com.moustache.professeur.balancetondechet.persistance;

import android.content.Context;

import com.moustache.professeur.balancetondechet.model.Trash;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveTrashes {

    public static void sauvegarderDechets (ArrayList<Trash> trashes, Context context) throws IOException {

        FileOutputStream fos = context.openFileOutput("saveFile.json", Context.MODE_PRIVATE);

        ObjectOutputStream os = new ObjectOutputStream(fos);

        os.writeObject(trashes);

        os.close();

        fos.close();

    }

}
