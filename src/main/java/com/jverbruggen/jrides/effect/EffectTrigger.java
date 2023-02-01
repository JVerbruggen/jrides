package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface EffectTrigger {
    void execute(Train train);
    void executeReversed(Train train);
    boolean finishedPlaying();
}
