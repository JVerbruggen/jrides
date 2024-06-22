package com.jverbruggen.jrides.animator.flatride.rotor.axis;

public class RotorAxisFactory {
    public static RotorAxis createAxisX(){
        return new RotorAxisX();
    }

    public static RotorAxis createAxisY(){
        return new RotorAxisY();
    }

    public static RotorAxis createAxisZ(){
        return new RotorAxisZ();
    }

    public static RotorAxis createAxisFromString(String rotorAxis){
        if(rotorAxis.equalsIgnoreCase("x"))
            return createAxisX();
        else if(rotorAxis.equalsIgnoreCase("y"))
            return createAxisY();
        else if(rotorAxis.equalsIgnoreCase("z"))
            return createAxisZ();
        else throw new RuntimeException("Unknown axis " + rotorAxis);
    }
}
