package com.moustache.professeur.balancetondechet.model;

import android.content.Context;

import java.util.ArrayList;

public class ListTrash extends ArrayList<Trash> {
    public ListTrash(Context ctx){
        addAll(Trash.parseMultipleFromJson(ctx));
    }

    public ListTrash(ListTrash lst){
        addAll(lst);
    }
}
