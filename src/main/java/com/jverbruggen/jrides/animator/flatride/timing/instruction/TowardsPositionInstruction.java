package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.models.math.SpeedUtil;
import org.bukkit.Bukkit;

public class TowardsPositionInstruction implements Instruction {
    private final float acceleration;
    private final float minSpeed;
    private final float maxSpeed;
    private final float towardsPosition;

    private Double originalPosition;

    public TowardsPositionInstruction(float acceleration, float minSpeed, float maxSpeed, float towardsPosition) {
        this.acceleration = acceleration;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.towardsPosition = towardsPosition;
        this.originalPosition = null;
    }

    public void execute(HasPosition hasPosition) {
        FlatRideComponentSpeed componentSpeed = hasPosition.getFlatRideComponentSpeed();

        if(Math.abs(hasPosition.getInstructionPosition() - towardsPosition) < 0.01){
            hasPosition.setInstructionPosition(towardsPosition);
            componentSpeed.setHard(0);
            return;
        }

        boolean positiveSpeed = componentSpeed.getSpeed() >= 0;
        boolean positiveAcceleration = this.acceleration >= 0;
        float accelerationAbs = Math.abs(this.acceleration);
        float minSpeed = positiveAcceleration ? this.minSpeed : this.maxSpeed;
        float maxSpeed = positiveAcceleration ? this.maxSpeed : this.minSpeed;

        boolean shouldRun = positiveSpeed == positiveAcceleration;

        if (shouldRun){
            if (originalPosition == null) {
                originalPosition = hasPosition.getInstructionPosition();
            }

            float lowerPosition = positiveAcceleration ? this.originalPosition.floatValue() : this.towardsPosition;
            float upperPosition = positiveAcceleration ? this.towardsPosition : this.originalPosition.floatValue();

            run(hasPosition, accelerationAbs, lowerPosition, upperPosition, 0f, maxSpeed, minSpeed);
        }else if(!positiveSpeed) {
            componentSpeed.accelerateTowards(accelerationAbs, 0);
        }else {
            componentSpeed.accelerateTowards(accelerationAbs, -.001f);
        }
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        execute((HasPosition) component);
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof HasPosition;
    }

    @Override
    public void reset() {
        originalPosition = null;
    }

    public static void run(HasPosition hasPosition, float acceleration, float lowerPosition, float upperPosition, float margin, float maxSpeed, float minSpeed){

        FlatRideComponentSpeed componentSpeed = hasPosition.getFlatRideComponentSpeed();
        float currentSpeed = componentSpeed.getSpeed();
        float absAcceleration = Math.abs(acceleration);

        boolean positiveAcceleration = acceleration >= 0;
        boolean goingForwards = currentSpeed > 0 || (currentSpeed == 0 && positiveAcceleration);

        float targetPositionAcc = positiveAcceleration ? upperPosition : lowerPosition;
        float fromPositionAcc = positiveAcceleration ? lowerPosition : upperPosition;
        float targetPositionSpd = goingForwards ? upperPosition : lowerPosition;
        float fromPositionSpd = goingForwards ? lowerPosition : upperPosition;

        float breakPosition = SpeedUtil.positionStartBraking(
                currentSpeed,
                goingForwards ? -absAcceleration : absAcceleration,
                targetPositionAcc,
                0);

//        JRidesPlugin.getLogger().debug("s: " + currentSpeed + "(" + goingForwards + ") a: " + acceleration);
//        JRidesPlugin.getLogger().debug("f: " + fromPositionAcc + " c: " + hasPosition.getInstructionPosition() + " t: " + targetPositionAcc + " b: " + breakPosition);

        boolean shouldBreak = hasPosition.hasPassed(fromPositionAcc, breakPosition, positiveAcceleration, margin);
        boolean shouldHardBreak = hasPosition.hasPassed(fromPositionSpd, targetPositionSpd, goingForwards, 0d);

//        JRidesPlugin.getLogger().debug("break: " + shouldBreak + " hard: " + shouldHardBreak + "\n----");

        if(shouldHardBreak){
            componentSpeed.setHard(0);
            hasPosition.setInstructionPosition(targetPositionSpd);
        }else if(shouldBreak){
            componentSpeed.accelerateTowards(absAcceleration, 0);
        }else{
            float targetSpeed = positiveAcceleration ? maxSpeed : minSpeed;
            componentSpeed.accelerateTowards(absAcceleration, targetSpeed);
        }
    }
}
