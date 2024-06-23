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

package com.jverbruggen.jrides.effect.external.animation;

import com.jverbruggen.jrides.animator.animatedjava.AnimatedJavaExecutor;
import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class AnimatedJavaEffectTrigger extends BaseTrainEffectTrigger {
    private final AnimatedJavaExecutor executor;
    private final String animationName;
    private final int despawnAfterTicks;

    public AnimatedJavaEffectTrigger(AnimatedJavaExecutor executor, String animationName, int despawnAfterTicks) {
        this.executor = executor;
        this.animationName = animationName;
        this.despawnAfterTicks = despawnAfterTicks;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        if(!executor.isSpawned()){
            executor.spawnRig();
        }
        executor.playAnimation(animationName);

        if(despawnAfterTicks != -1)
            Sync.runAfter(executor::removeRig, despawnAfterTicks);

        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
