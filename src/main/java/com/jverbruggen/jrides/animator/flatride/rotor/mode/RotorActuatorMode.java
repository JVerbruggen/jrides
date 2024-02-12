package com.jverbruggen.jrides.animator.flatride.rotor.mode;

import com.jverbruggen.jrides.animator.flatride.ActuatorMode;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;

public interface RotorActuatorMode extends ActuatorMode {
    void tick(FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis);
    double getRotation(RotorAxis rotorAxis);
    void setRotation(RotorAxis rotorAxis, double position);
}
