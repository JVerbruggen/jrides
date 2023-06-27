package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;

import java.util.List;

public class InstructionBinding implements TimingAction {
    private final Instruction instruction;
    private final List<FlatRideComponent> flatRideComponents;

    public InstructionBinding(Instruction instruction, List<FlatRideComponent> flatRideComponents) {
        this.instruction = instruction;
        this.flatRideComponents = flatRideComponents;
    }

    public void tick(){
        for(FlatRideComponent flatRideComponent : flatRideComponents){
            instruction.applyTo(flatRideComponent);
        }
    }
}
