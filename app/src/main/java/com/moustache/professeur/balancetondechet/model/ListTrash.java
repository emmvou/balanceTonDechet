package com.moustache.professeur.balancetondechet.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.moustache.professeur.balancetondechet.persistance.LoadTrashes;

import java.io.IOException;
import java.util.ArrayList;

public class ListTrash extends ArrayList<Trash> implements Parcelable {

    public ListTrash(Context ctx) throws IOException, ClassNotFoundException {
        //addAll(Trash.parseMultipleFromJson(ctx));
        addAll(LoadTrashes.chargerDechets(ctx));
    }

    public ListTrash(ListTrash lst){
        addAll(lst);
    }

    public ListTrash(){}

    protected ListTrash(Parcel in) {
        in.readList(this, this.getClass().getClassLoader());
    }

    public ArrayList<Trash> getArrayList(){
        ArrayList<Trash> arr = new ArrayList<>();
        arr.addAll(this);
        return arr;
    }

    public static final Creator<ListTrash> CREATOR = new Creator<ListTrash>() {
        @Override
        public ListTrash createFromParcel(Parcel in) {
            return new ListTrash(in);
        }

        @Override
        public ListTrash[] newArray(int size) {
            return new ListTrash[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this);
    }
}
