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
