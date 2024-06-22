package com.jverbruggen.jrides.effect.external;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class CommandAsPlayerEffectTrigger extends BaseTrainEffectTrigger {
    private final String command;

    public CommandAsPlayerEffectTrigger(String command) {
        this.command = command;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        for(Player player : train.getPassengers()){
            player.getBukkitPlayer().performCommand(command);
        }
        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
