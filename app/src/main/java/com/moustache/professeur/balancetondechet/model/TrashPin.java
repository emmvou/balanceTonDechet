package com.moustache.professeur.balancetondechet.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

public class TrashPin {
    private final double x;
    private final double y;

    public TrashPin(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance(double x, double y){
        return Math.sqrt(Math.pow(this.x - x,2)+Math.pow(this.y - y,2));
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

    public OverlayItem toOverlayItem() {
        return new OverlayItem("aTile", "aSnippet", toGeoPoint());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
