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

    private int wantsToBeNotified;
    private int metersFromTrashToTriggerNotification;
    private int notificationImportanceLevel;

    public User(String email, String nom, String prenom, boolean admin)
    {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.admin = (admin ? 1 : 0);
    }

    public User(String email, String nom, String prenom, boolean admin, int wantsToBeNotified, int metersFromTrashToTriggerNotification, int notificationImportanceLevel){
        this(email, nom, prenom, admin);
        this.wantsToBeNotified = wantsToBeNotified;
        this.metersFromTrashToTriggerNotification = metersFromTrashToTriggerNotification;
        this.notificationImportanceLevel = notificationImportanceLevel;
    }

    protected User(Parcel in) {
        email = in.readString();
        nom = in.readString();
        prenom = in.readString();
        wantsToBeNotified = in.readInt();
        metersFromTrashToTriggerNotification = in.readInt();
        notificationImportanceLevel = in.readInt();
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

    public int getWantsToBeNotified() {
        return wantsToBeNotified;
    }

    public int getMetersFromTrashToTriggerNotification() {
        return metersFromTrashToTriggerNotification;
    }

    public int getNotificationImportanceLevel() {
        return notificationImportanceLevel;
    }

    public void setWantsToBeNotified(int wantsToBeNotified) {
        this.wantsToBeNotified = wantsToBeNotified;
    }

    public void setMetersFromTrashToTriggerNotification(int metersFromTrashToTriggerNotification) {
        this.metersFromTrashToTriggerNotification = metersFromTrashToTriggerNotification;
    }

    public void setNotificationImportanceLevel(int notificationImportanceLevel) {
        this.notificationImportanceLevel = notificationImportanceLevel;
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
        dest.writeInt(wantsToBeNotified);
        dest.writeInt(metersFromTrashToTriggerNotification);
        dest.writeInt(notificationImportanceLevel);
        dest.writeInt(admin);
    }

    public void addTrash(Trash t){
        pickedUpTrashs.add(t);
    }

    public ArrayList<Trash> getPickedUpTrashs() {
        return pickedUpTrashs;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", pickedUpTrashs=" + pickedUpTrashs +
                ", wantsToBeNotified=" + wantsToBeNotified +
                ", metersFromTrashToTriggerNotification=" + metersFromTrashToTriggerNotification +
                ", notificationImportanceLevel=" + notificationImportanceLevel +
                '}';
    }
}
