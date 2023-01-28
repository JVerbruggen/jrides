package com.jverbruggen.jrides.config.trigger;

public enum TriggerType {
    MUSIC;

    public static TriggerType fromString(String typeString){
        if(typeString.equalsIgnoreCase("music")) return MUSIC;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
