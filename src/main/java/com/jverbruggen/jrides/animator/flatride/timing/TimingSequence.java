/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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

    public boolean tick(){
        if(current == null) return false;

        boolean finished = current.tick(state);
        if(finished) {
            current.cleanUp();
            loadNext();
            state = 0;
            return this.current == null;
        }else{
            state++;
            return false;
        }
    }

    public boolean isIdle(){
        return this.current == null;
    }
}
