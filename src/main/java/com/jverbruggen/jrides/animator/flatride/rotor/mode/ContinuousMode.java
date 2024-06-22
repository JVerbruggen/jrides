package com.jverbruggen.jrides.animator.flatride.rotor.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;

public class ContinuousMode implements RotorActuatorMode {
    private final Double lowerBound;
    private final Double upperBound;

    public ContinuousMode(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis){
        rotorAxis.addRotation(flatRideComponentSpeed.getSpeed());
    }

    @Override
    public double getRotation(RotorAxis rotorAxis) {
        return rotorAxis.getRotation();
    }

    @Override
    public void setRotation(RotorAxis rotorAxis, double position) {
        rotorAxis.setRotation(position);
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

}
