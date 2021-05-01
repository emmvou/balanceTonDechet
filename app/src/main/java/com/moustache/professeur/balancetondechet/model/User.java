package com.moustache.professeur.balancetondechet.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Parcelable, Serializable {
    private String email;
    private String nom;
    private String prenom;
    private int admin = 0;
    private ArrayList<Trash> pickedUpTrashs = new ArrayList<Trash>();

    public User(String email, String nom, String prenom){
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
    }

    public User(String email, String nom, String prenom, Boolean admin){
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.admin = (admin? 1 : 0);
    }

    protected User(Parcel in) {
        email = in.readString();
        nom = in.readString();
        prenom = in.readString();
        admin = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Boolean isAdmin() {return admin > 0;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeInt(admin);
    }

    public void addTrash(Trash t){
        pickedUpTrashs.add(t);
    }

    public ArrayList<Trash> getPickedUpTrashs() {
        return pickedUpTrashs;
    }
}
