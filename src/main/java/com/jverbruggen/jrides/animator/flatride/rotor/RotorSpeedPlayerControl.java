/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
