package com.jverbruggen.jrides.models.math;

public class SpeedUtil {
    public static float positionStartBraking(float currentSpeed, float acceleration, float targetPosition, float targetSpeed){
        float dSpeed = targetSpeed - currentSpeed;

        float timeUntilStandStill = dSpeed / acceleration;
        float decelerationDistance = (dSpeed * timeUntilStandStill) - (.5f * acceleration * (timeUntilStandStill*timeUntilStandStill));

        return targetPosition + decelerationDistance;
    }
}
