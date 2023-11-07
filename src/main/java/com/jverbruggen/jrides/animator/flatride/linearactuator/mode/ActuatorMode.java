package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.Vector3;

public interface ActuatorMode {
    void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState);
}
