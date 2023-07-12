package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasSpeed;

public class SpeedInstruction implements Instruction {
    private final float acceleration;
    private final float speed;

    public SpeedInstruction(float acceleration, float speed) {
        this.acceleration = acceleration;
        this.speed = speed;
    }

    public void execute(HasSpeed hasSpeed){
        FlatRideComponentSpeed speedComponent = hasSpeed.getFlatRideComponentSpeed();
        speedComponent.accelerateTowards(this.acceleration, this.speed);
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        execute((HasSpeed) component);
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof HasSpeed;
    }

    @Override
    public void reset() {

    }
}
