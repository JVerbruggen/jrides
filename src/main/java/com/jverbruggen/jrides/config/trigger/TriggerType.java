package com.jverbruggen.jrides.config.trigger;

public enum TriggerType {
    MUSIC,
    MULTI_ARMORSTAND_MOVEMENT,
    CART_ROTATE;

    public static TriggerType fromString(String typeString){
        if(typeString.equalsIgnoreCase("music")) return MUSIC;
        else if(typeString.equalsIgnoreCase("multi-armorstand-movement")) return MULTI_ARMORSTAND_MOVEMENT;
        else if(typeString.equalsIgnoreCase("cart-rotate")) return CART_ROTATE;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
