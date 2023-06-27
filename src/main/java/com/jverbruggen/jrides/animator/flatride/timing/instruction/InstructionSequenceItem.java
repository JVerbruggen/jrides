package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import java.util.List;

public class InstructionSequenceItem {
    private final int durationTicks;
    private final List<TimingAction> actions;

    public InstructionSequenceItem(int durationTicks, List<TimingAction> actions) {
        this.durationTicks = durationTicks;
        this.actions = actions;
    }

    public boolean tick(int state){
        if(state > durationTicks) return true;

        for(TimingAction action : actions){
            action.tick();
        }

        return false;
    }
}
