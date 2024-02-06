package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.Vector3;

public interface ActuatorMode {
    void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState);
    double getPosition(Vector3 actuatorState);
    void setPosition(Vector3 actuatorState, double position);
    double getLowerBound();
    double getUpperBound();
}
