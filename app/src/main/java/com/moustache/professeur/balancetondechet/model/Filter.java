package com.moustache.professeur.balancetondechet.model;

public class Filter {
    //m
    private double distance = 40075000;

    public static double EARTH_CIRCUMFERENCE = 40075000;

    public Filter(double distance) {
        if(distance < this.distance){
            this.distance = distance;
        }
    }

    public Filter(){

    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "distance=" + distance +
                '}';
    }
}
