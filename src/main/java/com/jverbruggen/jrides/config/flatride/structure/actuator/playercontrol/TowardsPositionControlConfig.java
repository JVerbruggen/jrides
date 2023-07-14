package com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorTargetPositionPlayerControl;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;

public class TowardsPositionControlConfig implements PlayerControlConfig {
    private final double lowerPosition;
    private final double upperPosition;
    private final double accelerate;

    public TowardsPositionControlConfig(double lowerPosition, double upperPosition, double accelerate) {
        this.lowerPosition = lowerPosition;
        this.upperPosition = upperPosition;
        this.accelerate = accelerate;
    }

    @Override
    public RotorPlayerControl createPlayerControl(){
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate);
    }
}
