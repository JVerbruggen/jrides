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
    public void tick() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void cleanUp(FlatRideComponent component) {

    }
}
