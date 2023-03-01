package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface TrainEffectTrigger extends EffectTrigger {
    void execute(Train train);
    void executeReversed(Train train);
}
