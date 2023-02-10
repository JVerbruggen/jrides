package com.jverbruggen.jrides.effect.handle;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class DefaultEffectTriggerHandle extends BaseEffectTriggerHandle{
    public DefaultEffectTriggerHandle(Frame frame, EffectTrigger effectTrigger) {
        super(frame, effectTrigger);
    }

    @Override
    public void execute(Train train) {
        getEffectTrigger().execute(train);
    }
}
