package com.jverbruggen.jrides.effect.entity;

import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;

public interface EntityMovementTrigger extends TrainEffectTrigger {
    void onFinish(Runnable runnable);
}
