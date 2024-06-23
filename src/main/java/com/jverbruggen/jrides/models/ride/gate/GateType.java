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

package com.jverbruggen.jrides.models.ride.gate;

public enum GateType {
    // Simple gate
    DOOR_OR_GATE,

    // Door block animations
    VANISH_BLOCK_DOOR,
    ANIMATED_BLOCK_DOOR,

    // If gate opening and closing is done by external logic (relying on feedback from that logic through events/notifications)
    EXTERNAL;

    public static GateType fromValue(String value){
        switch(value){
            case "default":
            case "door":
            case "gate":
                return GateType.DOOR_OR_GATE;
            default:
                throw new RuntimeException("Gate type " + value + " is (currently) not supported");
        }
    }
}
