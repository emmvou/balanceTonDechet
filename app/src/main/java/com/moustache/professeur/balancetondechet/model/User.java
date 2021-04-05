package com.moustache.professeur.balancetondechet.model;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String email;
    private String nom;
    private String prenom;

    public User(String email, String nom, String prenom){
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
    }

    protected User(Parcel in) {
        email = in.readString();
        nom = in.readString();
        prenom = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nom);
        dest.writeString(prenom);
    }
}