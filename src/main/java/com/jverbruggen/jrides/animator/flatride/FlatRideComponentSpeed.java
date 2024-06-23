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

package com.jverbruggen.jrides.animator.flatride;

public class FlatRideComponentSpeed {
    private double speed;
    private double minSpeed;
    private double maxSpeed;

    public FlatRideComponentSpeed(double speed, double minSpeed, double maxSpeed) {
        if(minSpeed > maxSpeed) throw new RuntimeException("Min speed has to be lower than max speed");
        else if(speed > maxSpeed) throw new RuntimeException("Speed has to be lower than max speed");
        else if(speed < minSpeed) throw new RuntimeException("Speed has to be higher than min speed");

        this.speed = speed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public FlatRideComponentSpeed(double speed){
        this.speed = 0;
        this.minSpeed = 0;
        this.maxSpeed = speed;
    }

    /**
     * Discouraged. This is an abrupt speed change.
     * Only use when absolutely necessary.
     */
    public void setHard(double speed){
        this.speed = speed;
    }

    public void accelerate(double acceleration){
        this.speed += acceleration;
        if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
        else if(this.speed < this.minSpeed) this.speed = this.minSpeed;
    }

    public void accelerateTowards(double acceleration, double towards){
        if(this.speed > towards){
            this.speed -= acceleration;
            if(this.speed < towards) this.speed = towards;
        }else if(this.speed < towards){
            this.speed += acceleration;
            if(this.speed > towards) this.speed = towards;
        }

        if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
        else if(this.speed < this.minSpeed) this.speed = this.minSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public FlatRideComponentSpeed clone(){
        return new FlatRideComponentSpeed(this.speed, this.minSpeed, this.maxSpeed);
    }
}
