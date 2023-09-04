package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.Vector3;

public class LinearMode implements ActuatorMode {
    private final Double lowerBound;
    private final Double upperBound;

    public LinearMode(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        double currentSpeed = flatRideComponentSpeed.getSpeed();
        actuatorState.y += currentSpeed;

        // TODO: implement braking
        if(upperBound != null && currentSpeed >= 0 && actuatorState.y > upperBound){
            actuatorState.y = upperBound;
            flatRideComponentSpeed.setHard(0);
        }else if(lowerBound != null && currentSpeed <= 0 && actuatorState.y < lowerBound){
            actuatorState.y = lowerBound;
            flatRideComponentSpeed.setHard(0);
        }
    }
}
