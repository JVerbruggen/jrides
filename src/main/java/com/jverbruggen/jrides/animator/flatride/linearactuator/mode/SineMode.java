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

public class SineMode implements LinearActuatorMode {
    private static final float PI = 3.1415926535f;
    private static final float PI2 = PI*2;

    private final float size;
    private final short phase;
    private double sineState;

    public SineMode(float size, short phase) {
        this.size = size;
        this.phase = phase;

        resetToInitialPhase();
    }

    public void tick(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        increaseSineState(flatRideComponentSpeed, actuatorState);
    }

    @Override
    public double getPosition(Vector3 actuatorState) {
        return sineState;
    }

    @Override
    public void setPosition(Vector3 actuatorState, double position) {
        this.updateActuatorState(actuatorState, position);
    }

    @Override
    public double getLowerBound() {
        return 0;
    }

    @Override
    public double getUpperBound() {
        return PI2;
    }

    private void resetToInitialPhase(){
        this.sineState = phase/180f*PI;
    }

    private void increaseSineState(FlatRideComponentSpeed flatRideComponentSpeed, Vector3 actuatorState){
        sineState = (sineState + flatRideComponentSpeed.getSpeed()/180*PI) % PI2;
        updateActuatorState(actuatorState, this.sineState);
    }

    private void updateActuatorState(Vector3 actuatorState, double sineState){
        actuatorState.y = Math.sin(sineState)*size;
    }
}
