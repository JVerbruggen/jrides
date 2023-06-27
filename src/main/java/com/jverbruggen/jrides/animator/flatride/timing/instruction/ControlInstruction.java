package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;

public class ControlInstruction implements Instruction {
    private final boolean allowControl;
    private final float controlSpeed;

    public ControlInstruction(boolean allowControl, float controlSpeed) {
        this.allowControl = allowControl;
        this.controlSpeed = controlSpeed;
    }

    public void execute(PlayerControllable playerControllable){
        playerControllable.setAllowControl(this.allowControl);
        playerControllable.setControlSpeed(this.controlSpeed);
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        execute((PlayerControllable) component);
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof PlayerControllable;
    }
}
