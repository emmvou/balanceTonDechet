package com.moustache.professeur.balancetondechet.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.Distance;
import org.osmdroid.views.overlay.OverlayItem;

public class TrashPin implements Parcelable {
    private final double x;
    private final double y;

    public TrashPin(double x, double y) {
        this.x = x;
        this.y= y;
    }

    protected TrashPin(Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
    }

    public static final Creator<TrashPin> CREATOR = new Creator<TrashPin>() {
        @Override
        public TrashPin createFromParcel(Parcel in) {
            return new TrashPin(in);
        }

        @Override
        public TrashPin[] newArray(int size) {
            return new TrashPin[size];
        }
    };

    //formula : https://www.movable-type.co.uk/scripts/latlong.html
    public double getDistance(double dx, double dy){
        double lat1 = x;
        double long1 = y;
        //return Math.sqrt(Distance.getSquaredDistanceToPoint(this.x, this.y, x, y));
        double R = 6371000;
        double phi1 = lat1*Math.PI/180;
        double phi2 = dx*Math.PI/180;
        double deltaphi = (dx-lat1)*Math.PI/180;
        double deltalambda = (dy - long1)*Math.PI/180;
        double a = Math.sin(deltaphi/2)*Math.sin(deltaphi/2)+Math.cos(phi1)*Math.cos(phi2)*Math.sin(deltalambda/2)*Math.sin(deltalambda/2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R*c; //m
        Log.v("DISTANCE", "distance between points : "+String.valueOf(d)+"m");
        return d/1000; //km
    }

    public TrashPin(JSONObject obj) throws JSONException {
        x = obj.getDouble("x");
        y = obj.getDouble("y");
    }

    @Override
    public String toString() {
        return "TrashPin{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    private GeoPoint toGeoPoint() {
        return new GeoPoint(x, y);
    }

    public OverlayItem toOverlayItem(Context context) {
        PinFactory.loadFromContext(context);
        return PinFactory.trashPin(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x);
        dest.writeDouble(y);
    }
}
