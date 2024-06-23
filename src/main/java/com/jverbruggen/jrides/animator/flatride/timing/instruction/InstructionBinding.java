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
        instruction.tick();
    }

    @Override
    public void cleanUp() {
        for(FlatRideComponent flatRideComponent : flatRideComponents){
            instruction.cleanUp(flatRideComponent);
        }
        instruction.reset();
    }
}
