package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractPlayerControl;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;

public class RotorSpeedPlayerControl extends AbstractPlayerControl implements RotorPlayerControl {
    private Rotor rotor;
    private final float lowerSpeed;
    private final float upperSpeed;
    private final float acceleration;

    private float currentSpeed;
    private float pendingAcceleration;

    public RotorSpeedPlayerControl(float lowerSpeed, float upperSpeed, float acceleration) {
        this.rotor = null;
        this.lowerSpeed = lowerSpeed;
        this.upperSpeed = upperSpeed;
        this.acceleration = acceleration;
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

        currentSpeed = rotor.getFlatRideComponentSpeed().getSpeed();
        float oldSpeed = currentSpeed;
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
