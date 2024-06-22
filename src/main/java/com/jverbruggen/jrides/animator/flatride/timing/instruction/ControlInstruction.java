package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;

public class ControlInstruction implements Instruction {
    private final boolean allowControl;

    public ControlInstruction(boolean allowControl) {
        this.allowControl = allowControl;
    }

    public void execute(PlayerControllable playerControllable){
        playerControllable.setAllowControl(this.allowControl);
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        execute((PlayerControllable) component);
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof PlayerControllable;
    }

    @Override
    public void tick() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void cleanUp(FlatRideComponent component) {

    }
}
