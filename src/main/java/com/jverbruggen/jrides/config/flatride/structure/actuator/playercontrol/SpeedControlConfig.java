package com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorSpeedPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ADControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ControlType;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.SpaceBarControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.WSControl;

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
    public RotorPlayerControl createADPlayerControl(){
        ControlType controlType = new ADControl();
        return new RotorSpeedPlayerControl(lowerSpeed, upperSpeed, accelerate, controlType);
    }

    @Override
    public RotorPlayerControl createWSPlayerControl() {
        return new RotorSpeedPlayerControl(lowerSpeed, upperSpeed, accelerate, new WSControl());
    }

    @Override
    public RotorPlayerControl createSpaceBarPlayerControl() {
        ControlType controlType = new SpaceBarControl();
        return new RotorSpeedPlayerControl(lowerSpeed, upperSpeed, accelerate, controlType);
    }
}
