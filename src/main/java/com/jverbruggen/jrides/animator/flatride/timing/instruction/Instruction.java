package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;

public interface Instruction {
    void applyTo(FlatRideComponent component);
    boolean canHandle(FlatRideComponent component);
    void reset();
    void cleanUp(FlatRideComponent component);
}
