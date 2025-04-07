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

import com.jverbruggen.jrides.animator.blender.BlenderAnimationExecutor;
import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.ride.CanSpawn;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class BlenderEffectTrigger extends BaseTrainEffectTrigger {
    private final BlenderAnimationExecutor executor;
    private final CanSpawn spawnTarget;
    private final int despawnAfterTicks;

    public BlenderEffectTrigger(BlenderAnimationExecutor executor, CanSpawn spawnTarget, int despawnAfterTicks) {
        this.executor = executor;
        this.spawnTarget = spawnTarget;
        this.despawnAfterTicks = despawnAfterTicks;
    }

    public BlenderEffectTrigger(BlenderAnimationExecutor executor) {
        this.executor = executor;
        this.spawnTarget = null;
        this.despawnAfterTicks = -1;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        if(spawnTarget != null && !spawnTarget.isSpawned()){
            spawnTarget.spawn();
        }
        executor.playAnimationFull();

        if(spawnTarget != null && despawnAfterTicks > -1)
            Sync.runAfter(spawnTarget::despawn, despawnAfterTicks);

        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
