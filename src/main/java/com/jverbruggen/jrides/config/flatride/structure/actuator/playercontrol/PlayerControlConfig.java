package com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;

public interface PlayerControlConfig {
    RotorPlayerControl createADPlayerControl();
    RotorPlayerControl createWSPlayerControl();
    RotorPlayerControl createSpaceBarPlayerControl();
}
