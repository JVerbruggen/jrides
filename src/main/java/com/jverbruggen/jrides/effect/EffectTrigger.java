package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public interface EffectTrigger {
    void execute(Train train);
    boolean finishedPlaying();
    Frame getFrame();

    void setNext(EffectTrigger effectTrigger);
    EffectTrigger next();
}
