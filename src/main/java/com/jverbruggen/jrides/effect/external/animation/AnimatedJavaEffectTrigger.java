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
