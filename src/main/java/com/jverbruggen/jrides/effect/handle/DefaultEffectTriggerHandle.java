package com.jverbruggen.jrides.effect.handle;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class DefaultEffectTriggerHandle extends BaseEffectTriggerHandle{
    public DefaultEffectTriggerHandle(Frame frame, EffectTrigger effectTrigger) {
        super(frame, effectTrigger);
    }

    @Override
    public void execute(Train train) {
        getEffectTrigger().execute(train);
    }
}
