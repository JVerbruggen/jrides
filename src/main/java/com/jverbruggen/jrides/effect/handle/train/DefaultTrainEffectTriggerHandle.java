package com.jverbruggen.jrides.effect.handle.train;

import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class DefaultTrainEffectTriggerHandle extends BaseTrainEffectTriggerHandle {
    public DefaultTrainEffectTriggerHandle(Frame frame, TrainEffectTrigger trainEffectTrigger) {
        super(frame, trainEffectTrigger);
    }

    @Override
    public boolean executeForTrain(Train train) {
        return getTrainEffectTrigger().execute(train);
    }
}
