package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface TrainEffectTrigger extends EffectTrigger {
    /**
     *
     * @param train
     * @return true if can process more effects
     */
    boolean execute(Train train);

    /**
     *
     * @param train
     * @return true if can process more effects
     */
    boolean executeReversed(Train train);
}
