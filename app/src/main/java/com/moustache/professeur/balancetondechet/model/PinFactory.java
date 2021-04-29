package com.moustache.professeur.balancetondechet.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.moustache.professeur.balancetondechet.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.Objects;

public class PinFactory {
    private static Drawable userLocationIcon;

    public static OverlayItem userPin(double x, double y) {
        OverlayItem pin = standardPin(x, y);
        setPositionIcon(pin);

        return pin;
    }

    public static OverlayItem trashPin(double x, double y) {
        return standardPin(x, y);
    }

    public static void loadFromContext(Context context) {
        if (Objects.isNull(userLocationIcon))
            userLocationIcon = AppCompatResources.getDrawable(context, R.drawable.ic_my_position);
    }

    private static OverlayItem standardPin(double x, double y) {
        GeoPoint p = new GeoPoint(x, y);
        return new OverlayItem("", "", p);
    }

    private static void setPositionIcon(OverlayItem pin) {
        pin.setMarker(userLocationIcon);
    }
}
