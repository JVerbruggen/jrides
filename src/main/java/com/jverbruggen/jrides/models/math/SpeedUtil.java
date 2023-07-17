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
