package com.jverbruggen.jrides.effect.handle.train;

import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class DefaultTrainEffectTriggerHandle extends BaseTrainEffectTriggerHandle {
    public DefaultTrainEffectTriggerHandle(Frame frame, TrainEffectTrigger trainEffectTrigger) {
        super(frame, trainEffectTrigger);
    }

    @Override
    public void execute(Train train) {
        getTrainEffectTrigger().execute(train);
    }
}
