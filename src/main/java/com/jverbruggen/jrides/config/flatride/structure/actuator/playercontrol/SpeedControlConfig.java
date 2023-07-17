package com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorSpeedPlayerControl;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;

public class SpeedControlConfig implements PlayerControlConfig {
    private final double lowerSpeed;
    private final double upperSpeed;
    private final double accelerate;

    public SpeedControlConfig(double lowerSpeed, double upperSpeed, double accelerate) {
        this.lowerSpeed = lowerSpeed;
        this.upperSpeed = upperSpeed;
        this.accelerate = accelerate;
    }

    @Override
    public RotorPlayerControl createPlayerControl(){
        return new RotorSpeedPlayerControl(lowerSpeed, upperSpeed, accelerate);
    }
}
