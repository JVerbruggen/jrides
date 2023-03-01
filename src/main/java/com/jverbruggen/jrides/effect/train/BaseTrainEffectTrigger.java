package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.DefaultTrainEffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.ReversedTrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;

public abstract class BaseTrainEffectTrigger implements TrainEffectTrigger {
    @Override
    public EffectTriggerHandle createHandle(Frame frame, boolean reversed) {
        if(reversed) {
            return new ReversedTrainEffectTriggerHandle(frame, this);
        } else {
            return new DefaultTrainEffectTriggerHandle(frame, this);
        }
    }
}
