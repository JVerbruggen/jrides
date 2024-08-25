/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
    BLENDER_ANIMATION,
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
        else if(typeString.equalsIgnoreCase("blender-animation")) return BLENDER_ANIMATION;
        else if(typeString.equalsIgnoreCase("structure")) return STRUCTURE;
        else if(typeString.equalsIgnoreCase("cart-rotate")) return CART_ROTATE;
        else if(typeString.equalsIgnoreCase("cart-restraint")) return CART_RESTRAINT;

        throw new RuntimeException("Trigger type " + typeString + " is not supported");
    }
}
