package com.jverbruggen.jrides.config.trigger;

import javax.annotation.Nullable;

public enum TriggerType {
    MUSIC,
    COMMAND,
    COMMAND_FOR_PLAYER,
    EXTERNAL_EVENT,
    MULTI_ENTITY_MOVEMENT,
    CART_ROTATE;


    public static TriggerType fromString(@Nullable String typeString){
        if(typeString == null) throw new RuntimeException("Cannot convert null to TriggerType");

        if(typeString.equalsIgnoreCase("music")) return MUSIC;
        else if(typeString.equalsIgnoreCase("command")) return COMMAND;
        else if(typeString.equalsIgnoreCase("command-for-player")) return COMMAND_FOR_PLAYER;
        else if(typeString.equalsIgnoreCase("external")) return EXTERNAL_EVENT;
        else if(typeString.equalsIgnoreCase("multi-entity-movement")) return MULTI_ENTITY_MOVEMENT;
        else if(typeString.equalsIgnoreCase("cart-rotate")) return CART_ROTATE;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
