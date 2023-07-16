package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public class MultiArmorstandMovementEffectTrigger extends BaseTrainEffectTrigger {
    private List<ArmorstandMovementEffectTrigger> armorstandMovements;

    public MultiArmorstandMovementEffectTrigger(List<ArmorstandMovementEffectTrigger> armorstandMovements) {
        this.armorstandMovements = armorstandMovements;
    }

    @Override
    public void execute(Train train) {
        armorstandMovements.forEach(a -> a.execute(train));
    }

    @Override
    public void executeReversed(Train train) {
        armorstandMovements.forEach(a -> a.executeReversed(train));
    }

    @Override
    public boolean finishedPlaying() {
        return armorstandMovements.stream().allMatch(ArmorstandMovementEffectTrigger::finishedPlaying);
    }
}
