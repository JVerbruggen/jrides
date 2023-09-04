package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

public enum LinearActuatorModeEnum {
    CONTINUOUS,
    SINE;

    public static LinearActuatorModeEnum from(String s) {
        return switch (s) {
            case "continuous" -> CONTINUOUS;
            case "sine" -> SINE;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
