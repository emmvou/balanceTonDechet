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

    public TrashPin(JSONObject obj) throws JSONException {
        x = obj.getDouble("x");
        y = obj.getDouble("y");
    }

    private GeoPoint toGeoPoint() {
        return new GeoPoint(x, y);
    }

    public OverlayItem toOverlayItem() {
        return new OverlayItem("aTile", "aSnippet", toGeoPoint());
    }
}
