package com.moustache.professeur.balancetondechet.model;

public enum Type{
    PORTABLE("Portable"),
    ENCOMBRANT("Encombrant"),
    TRES_ENCOMBRANT("Très encombrant");

    private String type;

    Type(String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public static Type fromString(String string){
        switch (string){
            case"Portable":
                return Type.PORTABLE;
            case"Encombrant":
                return Type.ENCOMBRANT;
            case "Très encombrant":
                return Type.TRES_ENCOMBRANT;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return type;
    }
}