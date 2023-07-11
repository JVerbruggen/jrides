package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.models.math.SpeedUtil;

public class TowardsPositionInstruction implements Instruction {
    private final float acceleration;
    private final float speed;
    private final float towardsPosition;

    private Double originalPosition;

    public TowardsPositionInstruction(float acceleration, float speed, float towardsPosition) {
        this.acceleration = acceleration;
        this.speed = speed;
        this.towardsPosition = towardsPosition;
        this.originalPosition = null;
    }

    public void execute(HasPosition hasPosition){
        if(originalPosition == null){
            originalPosition = hasPosition.getInstructionPosition();
        }

        FlatRideComponentSpeed speedComponent = hasPosition.getFlatRideComponentSpeed();

        float calcSpeed = speedComponent.getSpeed();
        float breakPosition = SpeedUtil.positionStartBraking(
                calcSpeed, -acceleration, towardsPosition, 0);

//        Bukkit.broadcastMessage("o: " + originalPosition + ", b: " + breakPosition + ", s: " + calcSpeed);

        if(hasPosition.hasPassed(originalPosition, breakPosition)){
            speedComponent.accelerateTowards(this.acceleration, 0);
        }else{
            speedComponent.accelerateTowards(this.acceleration, this.speed);
        }
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        execute((HasPosition) component);
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof HasPosition;
    }

    @Override
    public void reset() {
        originalPosition = null;
    }
}
