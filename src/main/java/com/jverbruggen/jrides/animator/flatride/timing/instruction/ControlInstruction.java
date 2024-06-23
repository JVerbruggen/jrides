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
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;

public class ControlInstruction implements Instruction {
    private final boolean allowControl;

    public ControlInstruction(boolean allowControl) {
        this.allowControl = allowControl;
    }

    public void execute(PlayerControllable playerControllable){
        playerControllable.setAllowControl(this.allowControl);
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        execute((PlayerControllable) component);
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof PlayerControllable;
    }

    @Override
    public void tick() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void cleanUp(FlatRideComponent component) {

    }
}
