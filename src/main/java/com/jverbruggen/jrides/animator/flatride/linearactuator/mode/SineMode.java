package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.Vector3;

public class SineMode implements ActuatorMode {
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

    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        increaseSineState(flatRideComponentSpeed, actuatorState);
    }

    private void resetToInitialPhase(){
        this.sineState = phase/180f*PI;
    }

    private void increaseSineState(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        sineState = (sineState + flatRideComponentSpeed.getSpeed()/180*PI) % PI2;
        updateActuatorState(actuatorState);
    }

    private void updateActuatorState(Vector3 actuatorState){
        actuatorState.y = Math.sin(sineState)*size;
    }
}
