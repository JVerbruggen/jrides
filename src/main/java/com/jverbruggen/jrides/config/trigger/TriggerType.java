package com.jverbruggen.jrides.config.trigger;

import javax.annotation.Nullable;

public enum TriggerType {
    RESET,
    MUSIC,
    COMMAND,
    EJECT,
    COMMAND_FOR_PLAYER,
    COMMAND_AS_PLAYER,
    EXTERNAL_EVENT,
    ANIMATION_SEQUENCE,
    ANIMATED_JAVA,
    STRUCTURE,
    CART_ROTATE,
    CART_RESTRAINT;


    public static TriggerType fromString(@Nullable String typeString){
        if(typeString == null) throw new RuntimeException("Cannot convert null to TriggerType");

        if(typeString.equalsIgnoreCase("reset")) return RESET;
        else if(typeString.equalsIgnoreCase("music")) return MUSIC;
        else if(typeString.equalsIgnoreCase("command")) return COMMAND;
        else if(typeString.equalsIgnoreCase("eject")) return EJECT;
        else if(typeString.equalsIgnoreCase("command-for-player")) return COMMAND_FOR_PLAYER;
        else if(typeString.equalsIgnoreCase("command-as-player")) return COMMAND_AS_PLAYER;
        else if(typeString.equalsIgnoreCase("external")) return EXTERNAL_EVENT;
        else if(typeString.equalsIgnoreCase("animation-sequence")) return ANIMATION_SEQUENCE;
        else if(typeString.equalsIgnoreCase("animated-java")) return ANIMATED_JAVA;
        else if(typeString.equalsIgnoreCase("structure")) return STRUCTURE;
        else if(typeString.equalsIgnoreCase("cart-rotate")) return CART_ROTATE;
        else if(typeString.equalsIgnoreCase("cart-restraint")) return CART_RESTRAINT;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
