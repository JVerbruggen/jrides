package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.cart.DefaultCartEffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.DefaultTrainEffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.ReversedTrainEffectTriggerHandle;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;

public abstract class BaseCartEffectTrigger implements CartEffectTrigger {
    @Override
    public EffectTriggerHandle createHandle(Frame frame, boolean reversed) {
        return new DefaultCartEffectTriggerHandle(frame, this);
    }
}
