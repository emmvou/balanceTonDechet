package com.moustache.professeur.balancetondechet.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.moustache.professeur.balancetondechet.persistance.LoadTrashes;
import com.moustache.professeur.balancetondechet.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trash implements Parcelable, Serializable {
    private TrashPin trashPin;
    private String name;
    private String desc;
    private String userEmail;
    private String imgPath;
    private boolean isPickedUp = false;
    private boolean isApproved = false;

    private static final String JSON_FILENAME = "trashes.json";

    public Trash (String name, String desc, TrashPin trashPin,String userEmail,String imgPath){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
        this.imgPath=imgPath;
        this.userEmail=userEmail;
    }

    private Trash (String name, String desc, TrashPin trashPin, boolean isPickedUp, boolean isApproved, String userEmail, String imgPath){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
        this.isApproved = isApproved;
        this.isPickedUp = isPickedUp;
        this.userEmail = userEmail;
        this.imgPath = imgPath;
    }

    protected Trash(Parcel in) {
        name = in.readString();
        desc = in.readString();
        userEmail = in.readString();
        isPickedUp = in.readByte() != 0;
        isApproved = in.readByte() != 0;
        trashPin = in.readParcelable(TrashPin.class.getClassLoader());
    }

    public static final Creator<Trash> CREATOR = new Creator<Trash>() {
        @Override
        public Trash createFromParcel(Parcel in) {
            return new Trash(in);
        }

        @Override
        public Trash[] newArray(int size) {
            return new Trash[size];
        }
    };

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public TrashPin getTrashPin() {
        return trashPin;
    }

    public boolean getApprobationStatus() {
        return isApproved;
    }

    public static List<Trash> parseMultipleFromJson(Context ctxt) {
        try {
            return LoadTrashes.chargerDechets(ctxt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Trash parseFromJson(JSONObject obj) throws JSONException {
        TrashPin pin = new TrashPin(obj);
        String name = obj.getString("name");
        String desc = obj.getString("desc");
        String userEmail = obj.getString("userEmail");
        boolean isPickedUp = obj.getBoolean("isPickedUp");
        boolean isApproved = obj.getBoolean("isApproved");
        String imgPath = obj.getString("imgPath");

        return new Trash(name, desc, pin, isPickedUp, isApproved,userEmail,imgPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(imgPath);
        dest.writeString(userEmail);
        dest.writeByte((byte) (isPickedUp ? 1 : 0));
        dest.writeByte((byte) (isApproved ? 1 : 0));
        dest.writeParcelable(trashPin, flags);
    }

    @Override
    public String toString() {
        return "Trash{" +
                "trashPin=" + trashPin +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", isPickedUp=" + isPickedUp +
                ", isApproved=" + isApproved +
                '}';
    }

    public String getImgPath() {
        return imgPath;
    }
}
