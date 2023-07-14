package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.models.math.SpeedUtil;

public class TowardsPositionInstruction implements Instruction {
    private double acceleration;
    private final double minSpeed;
    private final double maxSpeed;
    private final double towardsPosition;

    private Double originalPosition;

    public TowardsPositionInstruction(double acceleration, double minSpeed, double maxSpeed, double towardsPosition) {
        this.acceleration = acceleration;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.towardsPosition = towardsPosition;
        this.originalPosition = null;
    }

    public void execute(HasPosition hasPosition){
        if (originalPosition == null) {
            originalPosition = hasPosition.getInstructionPosition();

            double lowerOperatingRange = hasPosition.getLowerOperatingRange();
            boolean positiveFrom = lowerOperatingRange <= hasPosition.getUpperOperatingRange();

//            JRidesPlugin.getLogger().debug("l: " + lowerOperatingRange + ", x: " + originalPosition + ", t: " + towardsPosition + ", p: " + positiveFrom);

            if(!SpeedUtil.aboveInRange(lowerOperatingRange, originalPosition, towardsPosition, 0, true)){
                acceleration = -acceleration;
//                JRidesPlugin.getLogger().debug("Flipped " + acceleration);
            }
        }

        double lowerPosition = this.acceleration >= 0 ? this.originalPosition : this.towardsPosition;
        double upperPosition = this.acceleration >= 0 ? this.towardsPosition : this.originalPosition;

        TowardsPositionInstruction.run(
                hasPosition,
                this.acceleration,
                lowerPosition,
                upperPosition,
                0d,
                this.maxSpeed,
                this.minSpeed);
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
        acceleration = Math.abs(acceleration);
    }

    @Override
    public void cleanUp(FlatRideComponent component) {
        ((HasPosition)component).setInstructionPosition(towardsPosition);
    }

    public static void run(HasPosition hasPosition, double acceleration, double lowerPosition, double upperPosition, double margin, double maxSpeed, double minSpeed){
        FlatRideComponentSpeed componentSpeed = hasPosition.getFlatRideComponentSpeed();
        double currentSpeed = componentSpeed.getSpeed();
        double currentPosition = hasPosition.getInstructionPosition();
        double absAcceleration = Math.abs(acceleration);

        boolean positiveAcceleration = acceleration >= 0;
        boolean goingForwards = currentSpeed > 0 || (currentSpeed == 0 && positiveAcceleration);

        double targetPositionAcc = positiveAcceleration ? upperPosition : lowerPosition;
        double fromPositionAcc = positiveAcceleration ? lowerPosition : upperPosition;
        double targetPositionSpd = goingForwards ? upperPosition : lowerPosition;
        double fromPositionSpd = goingForwards ? lowerPosition : upperPosition;

        double breakPosition = SpeedUtil.positionStartBraking(
                currentSpeed,
                goingForwards ? -absAcceleration : absAcceleration,
                targetPositionAcc,
                0);

//        JRidesPlugin.getLogger().debug("s: " + currentSpeed + "(" + goingForwards + ") a: " + acceleration);
//        JRidesPlugin.getLogger().debug("f: " + fromPositionAcc + " c: " + hasPosition.getInstructionPosition() + " t: " + targetPositionAcc + " b: " + breakPosition);

        boolean shouldBreak = SpeedUtil.hasPassed(fromPositionAcc, hasPosition.getInstructionPosition(), breakPosition,
                positiveAcceleration, margin);
        boolean shouldHardBreak = SpeedUtil.hasPassed(fromPositionSpd, hasPosition.getInstructionPosition(), targetPositionSpd,
                goingForwards, absAcceleration);

//        JRidesPlugin.getLogger().debug("break: " + shouldBreak + " hard: " + shouldHardBreak + "\n----");

        if(shouldHardBreak){
            componentSpeed.setHard(0);
            if(Math.abs(targetPositionSpd - hasPosition.getInstructionPosition()) < margin)
                hasPosition.setInstructionPosition(targetPositionSpd);
        }else if(shouldBreak){
            componentSpeed.accelerateTowards(absAcceleration, 0);
            checkBump(currentPosition, hasPosition, componentSpeed, fromPositionAcc, targetPositionAcc, positiveAcceleration);
        }else{
            double targetSpeed = positiveAcceleration ? maxSpeed : minSpeed;

            componentSpeed.accelerateTowards(absAcceleration, targetSpeed);
            checkBump(currentPosition, hasPosition, componentSpeed, fromPositionAcc, targetPositionAcc, positiveAcceleration);
        }
    }

    private static void checkBump(double currentPosition, HasPosition hasPosition, FlatRideComponentSpeed componentSpeed, double fromPosition, double targetPosition, boolean positiveSpeed){
        double newPosition = currentPosition + componentSpeed.getSpeed();
        if(SpeedUtil.hasPassed(fromPosition, newPosition, targetPosition, positiveSpeed, 0d)){
            componentSpeed.setHard(0);
            hasPosition.setInstructionPosition(targetPosition);
        }
    }
}
