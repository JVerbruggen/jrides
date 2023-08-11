package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ControlType;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;

public class RotorSpeedPlayerControl extends AbstractPlayerControl implements RotorPlayerControl {
    private Rotor rotor;
    private final double lowerSpeed;
    private final double upperSpeed;
    private final double accelerate;

    private double currentSpeed;
    private double pendingAcceleration;

    public RotorSpeedPlayerControl(double lowerSpeed, double upperSpeed, double accelerate, ControlType controlType) {
        super(controlType);
        this.rotor = null;
        this.lowerSpeed = lowerSpeed;
        this.upperSpeed = upperSpeed;
        this.accelerate = accelerate;
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

        pendingAcceleration = getControlType().processInstruction(instruction, accelerate);
    }

    @Override
    public void apply() {
        double acceleration = this.pendingAcceleration; // Synchronization?

        currentSpeed = rotor.getFlatRideComponentSpeed().getSpeed();
        double oldSpeed = currentSpeed;
        currentSpeed += acceleration;
        if(currentSpeed > upperSpeed){
            currentSpeed = upperSpeed;
            acceleration = upperSpeed - oldSpeed;
        }
        else if(currentSpeed < lowerSpeed){
            currentSpeed = lowerSpeed;
            acceleration = lowerSpeed - oldSpeed;
        }

        rotor.getFlatRideComponentSpeed().accelerate(acceleration);
        this.pendingAcceleration = 0;
    }

    @Override
    public void reset() {
        this.pendingAcceleration = 0;
        this.currentSpeed = rotor.getFlatRideComponentSpeed().getSpeed();
    }

}
