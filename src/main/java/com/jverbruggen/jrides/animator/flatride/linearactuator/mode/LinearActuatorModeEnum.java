package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

public enum LinearActuatorModeEnum {
    LINEAR,
    SINE;

    public static LinearActuatorModeEnum from(String s) {
        return switch (s) {
            case "linear" -> LINEAR;
            case "sine" -> SINE;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
