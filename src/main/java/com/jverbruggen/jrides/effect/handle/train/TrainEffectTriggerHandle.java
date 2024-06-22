package com.jverbruggen.jrides.effect.handle.train;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface TrainEffectTriggerHandle extends EffectTriggerHandle {
    Frame getFrame();
    TrainEffectTrigger getTrainEffectTrigger();

    /**
     *
     * @param train
     * @return true if can process more effects
     */
    boolean executeForTrain(Train train);

    void setNext(TrainEffectTriggerHandle trainEffectTriggerHandle);
    TrainEffectTriggerHandle next();
}
