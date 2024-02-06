package com.jverbruggen.jrides.animator.flatride.timing.instruction.towards;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.Instruction;
import com.jverbruggen.jrides.models.math.SpeedUtil;
import org.bukkit.Bukkit;

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
            state.setAcceleration(accelerate);

            double lowerOperatingRange = hasPosition.getLowerOperatingRange();
            double upperOperatingRange = hasPosition.getUpperOperatingRange();

//            JRidesPlugin.getLogger().debug("l: " + lowerOperatingRange + ", x: " + state.getOriginalState() + ", t: " + towardsPosition + ", p: " + positiveFrom);

            if(!SpeedUtil.aboveInRange(lowerOperatingRange, state.getOriginalState(), towardsPosition, upperOperatingRange, true)){
                state.setAcceleration(-accelerate);
//                JRidesPlugin.getLogger().debug("Flipped " + state.getAcceleration());
            }
        }

        double acc = state.getAcceleration();
        double lowerPosition = acc >= 0 ? state.getOriginalState() : this.towardsPosition;
        double upperPosition = acc >= 0 ? this.towardsPosition : state.getOriginalState();

//        JRidesPlugin.getLogger().debug("" + lowerPosition + "," + upperPosition + "," + acc);
        TowardsPositionInstruction.run(
                hasPosition,
                acc,
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
        boolean positiveAcceleration = acceleration >= 0;

        double targetPosition = positiveAcceleration ? upperPosition : lowerPosition;
        double fromPosition = positiveAcceleration ? lowerPosition : upperPosition;

        hasPosition.goTowards(targetPosition, fromPosition, acceleration, componentSpeed);
    }
}
