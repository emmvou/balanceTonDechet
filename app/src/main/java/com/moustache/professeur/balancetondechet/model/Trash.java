package com.moustache.professeur.balancetondechet.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.moustache.professeur.balancetondechet.persistance.LoadTrashes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Trash implements Parcelable, Serializable {

    private TrashPin trashPin;
    private String name;
    private String desc;
    private String userEmail;
    private String imgPath;
    private Type type;
    private boolean isPickedUp;
    private boolean isApproved;

    private static final String JSON_FILENAME = "trashes.json";

    public Trash (String name, String desc, TrashPin trashPin,String userEmail,String imgPath, Type type){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
        this.imgPath=imgPath;
        this.userEmail=userEmail;
        this.type = type;
        isPickedUp = false;
        isApproved = false;
    }

    public Trash (String name, String desc, TrashPin trashPin, boolean isPickedUp, boolean isApproved, String userEmail, String imgPath,Type type){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
        this.isApproved = isApproved;
        this.isPickedUp = isPickedUp;
        this.userEmail = userEmail;
        this.imgPath = imgPath;
        this.type = type;
    }

    public void setApproved() {
        isApproved = true;
    }

    protected Trash(Parcel in) {
        name = in.readString();
        desc = in.readString();
        imgPath = in.readString();
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
        Type type = Type.valueOf(obj.getString("type"));

        return new Trash(name, desc, pin, isPickedUp, isApproved,userEmail,imgPath,type);
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
        dest.writeString(this.type.name());
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

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trash trash = (Trash) o;
        return isPickedUp == trash.isPickedUp &&
                isApproved == trash.isApproved &&
                Objects.equals(trashPin, trash.trashPin) &&
                Objects.equals(name, trash.name) &&
                Objects.equals(desc, trash.desc) &&
                Objects.equals(userEmail, trash.userEmail) &&
                Objects.equals(imgPath, trash.imgPath) &&
                Objects.equals(type,trash.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trashPin, name, desc, userEmail, imgPath, isPickedUp, isApproved);
    }
}
