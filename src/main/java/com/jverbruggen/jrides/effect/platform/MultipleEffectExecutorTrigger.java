package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public class MultipleEffectExecutorTrigger extends BaseTrainEffectTrigger {
    private final List<TrainEffectTrigger> triggers;

    public MultipleEffectExecutorTrigger(List<TrainEffectTrigger> triggers) {
        this.triggers = triggers;
    }

    @Override
    public void execute(Train train) {
        triggers.forEach(a -> a.execute(train));
    }

    @Override
    public void executeReversed(Train train) {
        triggers.forEach(a -> a.executeReversed(train));
    }

    @Override
    public boolean finishedPlaying() {
        return triggers.stream().allMatch(TrainEffectTrigger::finishedPlaying);
    }
}
