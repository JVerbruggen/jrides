package com.jverbruggen.jrides.animator.flatride.timing;

import com.jverbruggen.jrides.animator.flatride.timing.instruction.InstructionSequenceItem;

import java.util.Iterator;
import java.util.List;

public class TimingSequence {
    private final List<InstructionSequenceItem> items;

    private Iterator<InstructionSequenceItem> itemIterator;
    private InstructionSequenceItem current;
    private int state;

    public TimingSequence(List<InstructionSequenceItem> items) {
        this.items = items;
        this.state = 0;
        this.current = null;
    }

    public void restart(){
        if(this.current != null)
            return;

        this.state = 0;
        this.itemIterator = items.iterator();
        loadNext();
    }

    private void loadNext(){
        if(!itemIterator.hasNext())
            this.current = null;
        else
            this.current = itemIterator.next();
    }

    public void tick(){
        if(current == null) return;

        boolean finished = current.tick(state);
        if(finished) {
            loadNext();
            state = 0;
        }else state++;
    }

    public boolean isIdle(){
        return this.current == null;
    }
}
