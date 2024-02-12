package com.jverbruggen.jrides.animator.flatride.rotor.mode;

public enum RotorModeEnum {
    CONTINUOUS,
    SINE;

    public static RotorModeEnum from(String s) {
        return switch (s) {
            case "continuous" -> CONTINUOUS;
            case "sine" -> SINE;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
