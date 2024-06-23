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
