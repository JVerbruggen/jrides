package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ControlType;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.towards.TowardsPositionInstruction;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;

public class RotorTargetPositionPlayerControl extends AbstractPlayerControl implements RotorPlayerControl {
    private Rotor rotor;
    private final double lowerPosition;
    private final double upperPosition;
    private final double acceleration;
    private final double margin;

    private double pendingAcceleration;

    public RotorTargetPositionPlayerControl(double lowerPosition, double upperPosition, double acceleration, ControlType controlType) {
        super(controlType);
        this.rotor = null;
        this.lowerPosition = lowerPosition;
        this.upperPosition = upperPosition;
        this.acceleration = acceleration;
        this.margin = 2*this.acceleration;
        this.pendingAcceleration = 0f;
    }

    public void setRotor(Rotor rotor) {
        this.rotor = rotor;
        rotor.setLowerOperatingRange(lowerPosition);
        rotor.setUpperOperatingRange(upperPosition);
    }

    @Override
    public void processInstructionAsync(InstructionType instruction) {
        if(!rotor.allowsControl()) return;

        pendingAcceleration = getControlType().processInstruction(instruction, acceleration);
    }

    @Override
    public void apply() {
        TowardsPositionInstruction.run(
                rotor,
                this.pendingAcceleration,
                this.lowerPosition,
                this.upperPosition,
                this.margin,
                rotor.getFlatRideComponentSpeed().getMaxSpeed(),
                rotor.getFlatRideComponentSpeed().getMinSpeed());
    }

    @Override
    public void reset() {
        this.pendingAcceleration = 0;
    }

}
