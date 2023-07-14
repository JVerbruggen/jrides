package com.jverbruggen.jrides.animator.flatride.timing.instruction.towards;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.Instruction;
import com.jverbruggen.jrides.models.math.SpeedUtil;

import java.util.HashMap;
import java.util.Map;

public class TowardsPositionInstruction implements Instruction {
    private final double accelerate;
    private final double minSpeed;
    private final double maxSpeed;
    private final double towardsPosition;
    private final Map<HasPosition, TowardsPositionInstructionState> states;

    public TowardsPositionInstruction(double accelerate, double minSpeed, double maxSpeed, double towardsPosition) {
        this.accelerate = accelerate;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.towardsPosition = towardsPosition;
        this.states = new HashMap<>();
    }

    public void execute(HasPosition hasPosition){
        if(!states.containsKey(hasPosition)){
            TowardsPositionInstructionState state = new TowardsPositionInstructionState();
            states.put(hasPosition, state);
            execute(hasPosition, state);
        }else{
            execute(hasPosition, states.get(hasPosition));
        }
    }

    public void execute(HasPosition hasPosition, TowardsPositionInstructionState state){
        if (state.getOriginalState() == null) {
            state.setOriginalState(hasPosition.getInstructionPosition());

            double lowerOperatingRange = hasPosition.getLowerOperatingRange();

//            JRidesPlugin.getLogger().debug("l: " + lowerOperatingRange + ", x: " + state.getOriginalState() + ", t: " + towardsPosition + ", p: " + positiveFrom);

            if(!SpeedUtil.aboveInRange(lowerOperatingRange, state.getOriginalState(), towardsPosition, 0, true)){
                state.setAcceleration(-accelerate);
//                JRidesPlugin.getLogger().debug("Flipped " + state.getAcceleration());
            }
        }

        double lowerPosition = state.getAcceleration() >= 0 ? state.getOriginalState() : this.towardsPosition;
        double upperPosition = state.getAcceleration() >= 0 ? this.towardsPosition : state.getOriginalState();

        TowardsPositionInstruction.run(
                hasPosition,
                state.getAcceleration(),
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
        states.values().forEach(s -> {
                s.setAcceleration(accelerate);
                s.setOriginalState(null);
            }
        );
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
