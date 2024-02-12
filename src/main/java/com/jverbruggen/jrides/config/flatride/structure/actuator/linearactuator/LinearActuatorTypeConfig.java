package com.jverbruggen.jrides.config.flatride.structure.actuator.linearactuator;

import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.LinearActuatorMode;

public interface LinearActuatorTypeConfig {
    LinearActuatorMode createActuatorMode();
}
