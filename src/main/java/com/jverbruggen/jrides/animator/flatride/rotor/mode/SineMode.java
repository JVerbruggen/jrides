package com.jverbruggen.jrides.animator.flatride.rotor.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
import org.bukkit.Bukkit;

public class SineMode implements RotorActuatorMode {
    private static final float PI = 3.1415926535f;
    private static final float PI2 = PI*2;

    private final float size;
    private final short phase;
    private double sineState;

    public SineMode(float size, short phase) {
        this.size = size;
        this.phase = phase;

        resetToInitialPhase();
    }

    @Override
    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis) {
        increaseSineState(flatRideComponentSpeed, rotorAxis);
    }

    @Override
    public double getLowerBound() {
        return 0;
    }

    @Override
    public double getUpperBound() {
        return PI2;
    }

    private void resetToInitialPhase(){
        this.sineState = phase/180f*PI;
    }

    private void increaseSineState(FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis){
        sineState = (sineState + flatRideComponentSpeed.getSpeed()/180*PI) % PI2;
        updateRotorAxis(rotorAxis, this.sineState);
    }

    private void updateRotorAxis(RotorAxis rotorAxis, double sineState){
        rotorAxis.setRotation(Math.sin(sineState)*size);
    }

    @Override
    public double getRotation(RotorAxis rotorAxis) {
        return sineState;
    }

    @Override
    public void setRotation(RotorAxis rotorAxis, double position) {
        this.updateRotorAxis(rotorAxis, position);
    }
}
