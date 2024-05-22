package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class ResetEffectTrigger extends BaseTrainEffectTrigger {
    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        train.reset();
        return false;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
