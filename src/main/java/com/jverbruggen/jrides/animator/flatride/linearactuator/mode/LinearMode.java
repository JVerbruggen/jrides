package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.Vector3;

public class LinearMode implements ActuatorMode {
    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        actuatorState.y += flatRideComponentSpeed.getSpeed();
    }
}
