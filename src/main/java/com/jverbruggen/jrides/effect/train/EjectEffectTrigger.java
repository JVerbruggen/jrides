package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.stream.Collectors;

public class EjectEffectTrigger extends BaseTrainEffectTrigger {
    private final boolean asFinished;

    public EjectEffectTrigger(boolean asFinished) {
        this.asFinished = asFinished;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        if(asFinished)
            PlayerFinishedRideEvent.sendFinishedRideEvent(train.getPassengers()
                .stream()
                .map(p -> (JRidesPlayer)p)
                .collect(Collectors.toList()), train.getHandle().getCoasterHandle().getRide());

        train.ejectPassengers();
        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
