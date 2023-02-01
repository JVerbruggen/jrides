package com.jverbruggen.jrides.effect.handle;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface EffectTriggerHandle {
    Frame getFrame();
    EffectTrigger getEffectTrigger();
    void execute(Train train);

    void setNext(EffectTriggerHandle effectTriggerHandle);
    EffectTriggerHandle next();
}
