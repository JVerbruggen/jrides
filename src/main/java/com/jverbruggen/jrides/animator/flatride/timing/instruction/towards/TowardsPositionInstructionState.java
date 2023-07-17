package com.jverbruggen.jrides.animator.flatride.timing.instruction.towards;

public class TowardsPositionInstructionState {
    private Double originalState;
    private double acceleration;

    public TowardsPositionInstructionState() {
        this.originalState = null;
        this.acceleration = 0d;
    }

    public Double getOriginalState() {
        return originalState;
    }

    public void setOriginalState(Double originalState) {
        this.originalState = originalState;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
}
