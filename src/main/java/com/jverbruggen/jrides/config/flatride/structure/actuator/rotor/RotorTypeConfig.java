package com.jverbruggen.jrides.config.flatride.structure.actuator.rotor;

import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorActuatorMode;

public interface RotorTypeConfig {
    RotorActuatorMode createActuatorMode();
}
