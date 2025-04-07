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

import com.jverbruggen.jrides.animator.blender.BlenderAnimationExecutor;
import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.models.ride.Has6DOFPosition;

public class AnimationInstruction implements Instruction {
    private final BlenderAnimationExecutor blenderAnimationExecutor;

    public AnimationInstruction(BlenderAnimationExecutor blenderAnimationExecutor) {
        this.blenderAnimationExecutor = blenderAnimationExecutor;
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        BlenderExportPositionRecord position = blenderAnimationExecutor
                .withAnimationTarget(component.getIdentifier())
                .getPositionRecordOrNull();

        if(position == null) return;

        ((Has6DOFPosition)component).setPositionRotation(
                position.toMinecraftVector(),
                position.toMinecraftQuaternion());
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof Has6DOFPosition;
    }

    @Override
    public void tick() {
        blenderAnimationExecutor.nextFrame();
    }

    @Override
    public void reset() {
        blenderAnimationExecutor.reset();
    }

    @Override
    public void cleanUp(FlatRideComponent component) {

    }
}
