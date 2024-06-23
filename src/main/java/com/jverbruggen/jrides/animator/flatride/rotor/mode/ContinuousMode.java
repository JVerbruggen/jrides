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

package com.jverbruggen.jrides.animator.flatride.rotor.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;

public class ContinuousMode implements RotorActuatorMode {
    private final Double lowerBound;
    private final Double upperBound;

    public ContinuousMode(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis){
        rotorAxis.addRotation(flatRideComponentSpeed.getSpeed());
    }

    @Override
    public double getRotation(RotorAxis rotorAxis) {
        return rotorAxis.getRotation();
    }

    @Override
    public void setRotation(RotorAxis rotorAxis, double position) {
        rotorAxis.setRotation(position);
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

}
