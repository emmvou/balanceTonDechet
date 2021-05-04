package com.moustache.professeur.balancetondechet.model;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    //m
    private double distance = 40075000;
    private boolean portable = true;
    private boolean encombrant = true;
    private boolean tresEncombrant = true;

    public static double EARTH_CIRCUMFERENCE = 40075000;

    public Filter(double distance) {
        if(distance < this.distance){
            this.distance = distance;
        }
    }

    public Filter(double distance, boolean portable, boolean encombrant, boolean tresEncombrant){
        this(distance);
        this.portable = portable;
        this.encombrant = encombrant;
        this.tresEncombrant = tresEncombrant;
    }

    public Filter(){

    }

    public Filter(boolean portable, boolean encombrant, boolean tresEncombrant) {
        this.portable = portable;
        this.encombrant = encombrant;
        this.tresEncombrant = tresEncombrant;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isPortable() {
        return portable;
    }

    public boolean isEncombrant() {
        return encombrant;
    }

    public boolean isTresEncombrant() {
        return tresEncombrant;
    }

    public List<Type> selectedTypes(){
        List<Type> lst = new ArrayList<Type>();
        if(isPortable())
            lst.add(Type.PORTABLE);
        if(isEncombrant())
            lst.add(Type.ENCOMBRANT);
        if(isTresEncombrant())
            lst.add(Type.TRES_ENCOMBRANT);
        return lst;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "distance=" + distance +
                ", portable=" + portable +
                ", encombrant=" + encombrant +
                ", tresEncombrant=" + tresEncombrant +
                '}';
    }
}
