package com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorTargetPositionPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ADControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ControlType;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.SpaceBarControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.WSControl;

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
    public RotorPlayerControl createADPlayerControl(){
        ControlType controlType = new ADControl();
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate, controlType);
    }

    @Override
    public RotorPlayerControl createWSPlayerControl() {
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate, new WSControl());
    }

    @Override
    public RotorPlayerControl createSpaceBarPlayerControl() {
        ControlType controlType = new SpaceBarControl();
        return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, accelerate, controlType);
    }
}
