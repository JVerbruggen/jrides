package com.jverbruggen.jrides.config.trigger;

public enum TriggerType {
    MUSIC,
    MULTI_ARMORSTAND_MOVEMENT;

    public static TriggerType fromString(String typeString){
        if(typeString.equalsIgnoreCase("music")) return MUSIC;
        else if(typeString.equalsIgnoreCase("multi-armorstand-movement")) return MULTI_ARMORSTAND_MOVEMENT;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
