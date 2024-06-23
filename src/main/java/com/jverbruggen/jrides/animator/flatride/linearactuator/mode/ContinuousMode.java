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

package com.jverbruggen.jrides.animator.flatride.linearactuator.mode;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.models.math.Vector3;

public class ContinuousMode implements LinearActuatorMode {
    private final Double lowerBound;
    private final Double upperBound;

    public ContinuousMode(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        double currentSpeed = flatRideComponentSpeed.getSpeed();
        actuatorState.y += currentSpeed;

        // TODO: implement braking
        if(upperBound != null && currentSpeed >= 0 && actuatorState.y > upperBound){
            actuatorState.y = upperBound;
            flatRideComponentSpeed.setHard(0);
        }else if(lowerBound != null && currentSpeed < 0 && actuatorState.y < lowerBound){
            actuatorState.y = lowerBound;
            flatRideComponentSpeed.setHard(0);
        }
    }

    @Override
    public double getPosition(Vector3 actuatorState) {
        return actuatorState.y;
    }

    @Override
    public void setPosition(Vector3 actuatorState, double position) {
        actuatorState.y = position;
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
