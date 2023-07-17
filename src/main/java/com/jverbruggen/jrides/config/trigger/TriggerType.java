package com.jverbruggen.jrides.config.trigger;

import javax.annotation.Nullable;

public enum TriggerType {
    MUSIC,
    COMMAND,
    EXTERNAL_EVENT,
    MULTI_ARMORSTAND_MOVEMENT,
    CART_ROTATE;


    public static TriggerType fromString(@Nullable String typeString){
        if(typeString == null) throw new RuntimeException("Cannot convert null to TriggerType");

        if(typeString.equalsIgnoreCase("music")) return MUSIC;
        else if(typeString.equalsIgnoreCase("command")) return COMMAND;
        else if(typeString.equalsIgnoreCase("multi-armorstand-movement")) return MULTI_ARMORSTAND_MOVEMENT;
        else if(typeString.equalsIgnoreCase("cart-rotate")) return CART_ROTATE;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
