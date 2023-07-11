package com.jverbruggen.jrides.animator.flatride.interfaces;

public interface HasPosition extends HasSpeed {
    boolean hasPassed(double from, double target);
    default boolean hasPassed(double target){
        return hasPassed(0, target);
    }
    void setInstructionPosition(double position);

    double getInstructionPosition();
}
