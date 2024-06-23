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

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.interfaces.Component6DOFPosition;
import com.jverbruggen.jrides.state.ride.flatride.Animation;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;

public class AnimationInstruction implements Instruction {
    private final AnimationHandle animationHandle;
    private int frameIndexState;

    public AnimationInstruction(AnimationHandle animationHandle) {
        this.animationHandle = animationHandle;
        this.frameIndexState = 0;
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        Animation animation = animationHandle.getAnimation(component.getIdentifier());
        if(animation.getFrames().size() <= frameIndexState) return;

        BlenderExportPositionRecord position = animation.getFrames().get(frameIndexState);

        ((Component6DOFPosition)component).setPositionRotation(
                position.toMinecraftVector(),
                position.toMinecraftQuaternion());
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof Component6DOFPosition;
    }

    @Override
    public void tick() {
        frameIndexState++;
    }

    @Override
    public void reset() {
        frameIndexState = 0;
    }

    @Override
    public void cleanUp(FlatRideComponent component) {

    }
}
