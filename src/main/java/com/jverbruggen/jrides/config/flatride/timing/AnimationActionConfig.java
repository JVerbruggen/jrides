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

package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.animator.blender.BlenderAnimationExecutor;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.AnimationInstruction;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.InstructionBinding;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.TimingAction;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;
import com.jverbruggen.jrides.state.ride.flatride.AnimationLoader;

import java.util.ArrayList;
import java.util.List;

public class AnimationActionConfig extends BaseConfig implements ActionConfig{
    private final String animation;
    private final String targetIdentifier;

    public AnimationActionConfig(String animation, String targetIdentifier) {
        this.animation = animation;
        this.targetIdentifier = targetIdentifier;
    }

    public String getAnimation() {
        return animation;
    }

    @Override
    public List<TimingAction> getTimingAction(FlatRideHandle flatRideHandle, List<FlatRideComponent> flatRideComponents) {
        List<FlatRideComponent> targetedFlatRideComponents = flatRideComponents.stream()
                .filter(c -> c.equalsToIdentifier(targetIdentifier))
                .toList();

        List<TimingAction> timingActions = new ArrayList<>();
        AnimationLoader animationLoader = ServiceProvider.getSingleton(AnimationLoader.class);

        AnimationHandle animationHandle = animationLoader.loadFlatRideAnimation(this.animation, flatRideHandle);
        BlenderAnimationExecutor blenderAnimationExecutor = new BlenderAnimationExecutor(animationHandle);
        timingActions.add(new InstructionBinding(new AnimationInstruction(blenderAnimationExecutor), targetedFlatRideComponents));

        return timingActions;
    }
}
