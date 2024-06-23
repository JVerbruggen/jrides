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

package com.jverbruggen.jrides.models.math;

public class SpeedUtil {
    public static double positionStartBraking(double currentSpeed, double acceleration, double targetPosition, double targetSpeed){
        double dSpeed = targetSpeed - currentSpeed;

        double timeUntilStandStill = dSpeed / acceleration;
        double decelerationDistance = (dSpeed * timeUntilStandStill) - (.5f * acceleration * (timeUntilStandStill*timeUntilStandStill));

        return targetPosition + decelerationDistance;
    }

    public static boolean hasPassed(double from, double currentPosition, double to, boolean positiveFrom) {
        return hasPassed(from, currentPosition, to, positiveFrom, 0d);
    }

    public static boolean hasPassed(double from, double currentPosition, double to, boolean positiveFrom, double margin) {
        if(positiveFrom){
            return !inRange(from, currentPosition, to) || currentPosition == to;
        }else{
            return !inRange(-from, -currentPosition, -to) || currentPosition == to;
        }
    }

    public static boolean aboveInRange(double from, double currentPosition, double target, double to, boolean positiveFrom){
        if(positiveFrom)
            return inRange(from, currentPosition, target);
        else
            return inRange(-from, -currentPosition, -target);
    }

    public static boolean inRange(double rangeLower, double x, double rangeUpper){
        boolean normalBetweenState = rangeLower <= rangeUpper;
        boolean rotateBetweenState = rangeLower > rangeUpper;

        boolean inNormalState = normalBetweenState && (rangeLower <= x && x <= rangeUpper);
        boolean inRotateState = rotateBetweenState && (rangeLower <= x || x <= rangeUpper);

        return inNormalState || inRotateState;
    }
}
