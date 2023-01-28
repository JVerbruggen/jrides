package com.jverbruggen.jrides.config.trigger;

public enum TriggerType {
    MUSIC,
    MULTI_ARMORSTAND_MOVEMENT,
    MOVING_FALLING_BLOCK_PLATFORM;

    public static TriggerType fromString(String typeString){
        if(typeString.equalsIgnoreCase("music")) return MUSIC;
        else if(typeString.equalsIgnoreCase("multi-armorstand-movement")) return MULTI_ARMORSTAND_MOVEMENT;
        else if(typeString.equalsIgnoreCase("moving-falling-block-platform")) return MOVING_FALLING_BLOCK_PLATFORM;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
