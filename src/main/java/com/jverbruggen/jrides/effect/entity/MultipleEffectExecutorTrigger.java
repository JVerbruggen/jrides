package com.jverbruggen.jrides.effect.entity;

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
    public boolean execute(Train train) {
        return triggers.stream().allMatch(a -> a.execute(train));
    }

    @Override
    public boolean executeReversed(Train train) {
        return triggers.stream().allMatch(a -> a.executeReversed(train));
    }

    @Override
    public boolean finishedPlaying() {
        return triggers.stream().allMatch(TrainEffectTrigger::finishedPlaying);
    }
}
