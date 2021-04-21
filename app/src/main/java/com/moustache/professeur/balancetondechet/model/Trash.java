package com.moustache.professeur.balancetondechet.model;

public class Trash {
    private TrashPin trashPin;
    private String name;
    private String desc;
    private boolean isPickedUp = false;

    public Trash (String name, String desc, TrashPin trashPin){
        this.name=name;
        this.desc = desc;
        this.trashPin = trashPin;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public TrashPin getTrashPin() {
        return trashPin;
    }

}
