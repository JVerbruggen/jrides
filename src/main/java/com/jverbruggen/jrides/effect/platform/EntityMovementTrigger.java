package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;

public interface EntityMovementTrigger extends TrainEffectTrigger {
    void onFinish(Runnable runnable);
}
