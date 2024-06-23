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

package com.jverbruggen.jrides.animator.flatride.rotor.axis;

import com.jverbruggen.jrides.models.math.MathUtil;
import com.jverbruggen.jrides.models.math.Quaternion;

public class RotorAxisY implements RotorAxis {
    private double value;

    public RotorAxisY() {
        this.value = 0;
    }

    protected void add(double value){
        this.value = MathUtil.floorMod(this.value + value, 360d);
    }

    @Override
    public void setRotation(double toValue){
        double fromRotation = getRotation();
        if(fromRotation == toValue) return;

        addRotation(toValue - fromRotation);
    }

    @Override
    public void addRotation(double addValue) {
        add(addValue);
    }

    @Override
    public double getRotation() {
        return this.value;
    }

    @Override
    public Quaternion getQuaternion() {
        return Quaternion.fromYawPitchRoll(0, this.value, 0);
    }
}
