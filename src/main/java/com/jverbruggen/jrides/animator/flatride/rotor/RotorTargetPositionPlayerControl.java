package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.flatride.AbstractPlayerControl;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.MathUtil;
import com.jverbruggen.jrides.models.math.SpeedUtil;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import org.bukkit.Bukkit;

public class RotorTargetPositionPlayerControl extends AbstractPlayerControl implements RotorPlayerControl {
    private Rotor rotor;
    private final float lowerPosition;
    private final float upperPosition;
    private final float acceleration;
    private final float margin;

    private float currentSpeed;
    private float pendingAcceleration;

    public RotorTargetPositionPlayerControl(float lowerPosition, float upperPosition, float acceleration) {
        this.rotor = null;
        this.lowerPosition = lowerPosition;
        this.upperPosition = upperPosition;
        this.acceleration = acceleration;
//        this.margin = 4*this.acceleration;
        this.margin = 2*this.acceleration;
        this.currentSpeed = 0f;
        this.pendingAcceleration = 0f;
    }

    public void setRotor(Rotor rotor) {
        this.rotor = rotor;
        this.currentSpeed = this.rotor.getFlatRideComponentSpeed().getSpeed();
    }

    @Override
    public void processInstructionAsync(InstructionType instruction) {
        if(!rotor.allowsControl()) return;

        if(instruction == InstructionType.A){
            pendingAcceleration = -acceleration;
        }else if(instruction == InstructionType.D){
            pendingAcceleration = acceleration;
        }
    }

    @Override
    public void apply() {
        float acceleration = this.pendingAcceleration; // Synchronization?

        FlatRideComponentSpeed componentSpeed = rotor.getFlatRideComponentSpeed();

        currentSpeed = componentSpeed.getSpeed();

        boolean positiveAcceleration = acceleration >= 0;
        boolean positiveSpeed = currentSpeed > 0 || (currentSpeed == 0 && positiveAcceleration);

        float targetPosition = positiveAcceleration ? this.upperPosition : this.lowerPosition;
        float fromPosition = positiveAcceleration ? this.lowerPosition : this.upperPosition;

        float breakPosition = SpeedUtil.positionStartBraking(
                currentSpeed,
                positiveSpeed ? -this.acceleration : this.acceleration,
                targetPosition,
                0);

//        JRidesPlugin.getLogger().debug("s: " + currentSpeed + "(" + positiveSpeed + ") a: " + acceleration);
//        JRidesPlugin.getLogger().debug("f: " + fromPosition + " t: " + targetPosition + " b: " + breakPosition);

        boolean shouldBreak = rotor.hasPassed(fromPosition, breakPosition, positiveAcceleration, this.margin);
        boolean shouldHardBreak = rotor.hasPassed(fromPosition, targetPosition, positiveAcceleration, 0d);

//        JRidesPlugin.getLogger().debug("break: " + shouldBreak + " hard: " + shouldHardBreak + "\n----");

        if(shouldHardBreak){
            rotor.getFlatRideComponentSpeed().setHard(0);
            rotor.setRotorRotation(targetPosition);
        }else if(shouldBreak){
            rotor.getFlatRideComponentSpeed().accelerateTowards(this.acceleration, 0);
        }else{
            float targetSpeed = positiveAcceleration ? componentSpeed.getMaxSpeed() : componentSpeed.getMinSpeed();
            rotor.getFlatRideComponentSpeed().accelerateTowards(Math.abs(acceleration), targetSpeed);
        }
    }

    @Override
    public void reset() {
        this.pendingAcceleration = 0;
        this.currentSpeed = rotor.getFlatRideComponentSpeed().getSpeed();
    }

}
